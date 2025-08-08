# Copyright (C) 2020 Cliff Brake <cbrake@bec-systems.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Simple IOT Framework application"
HOMEPAGE = "http://simpleiot.org"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE;md5=3b83ef96387f14655fc854ddc3c6bd57"

SRC_URI = "git://${GO_IMPORT};protocol=https;branch=master;destsuffix=${GO_SRCURI_DESTSUFFIX}"

SRCREV = "fc4f6084da237d168a48409447b80a114c8c1029"

require ${BPN}-licenses.inc
require ${BPN}-go-mods.inc

GO_IMPORT = "github.com/simpleiot/simpleiot"
GO_INSTALL = "${GO_IMPORT}/cmd/siot"

inherit go-mod go-mod-update-modules pkgconfig systemd update-rc.d

CGO_ENABLED:x86-64 = "0"
export CGO_ENABLED

INITSCRIPT_NAME = "siot"
INITSCRIPT_PARAMS = "start 99 5 . stop 20 6 ."

INHIBIT_PACKAGE_STRIP = "1"

do_install:append() {
    rm -rf ${D}${libdir}/go/src/${GO_IMPORT}/cmd/edge/edge
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -D -m 0644 ${S}/src/${GO_IMPORT}/contrib/siot.service ${D}${systemd_unitdir}/system/siot.service
        sed -i "s:ExecStart=/usr/bin/siot:ExecStart=/bin/sh -c \"cd /data; /usr/bin/siot\":" \
            ${D}${systemd_unitdir}/system/siot.service
    else
        install -D -m 0755 ${S}/src/${GO_IMPORT}/contrib/siot.init ${D}${sysconfdir}/init.d/siot
    fi
}

SYSTEMD_SERVICE:${PN} = "siot.service"

INSANE_SKIP:${PN} += "ldflags already-stripped"

# we use sqlite and not all architectures seem to be supported see
# https://pkg.go.dev/modernc.org/sqlite#hdr-Supported_platforms_and_architectures
COMPATIBLE_HOST = "(x86_64.*|i.86.*|arm.*|aarch64.*|riscv64.*|powerpc64le.*)-(linux)"

# /usr/lib/go/src/github.com/simpleiot/simpleiot/tools/testloop.sh contained in package simpleiot-dev requires /bin/bash,
RDEPENDS:${PN}-dev += "bash"
