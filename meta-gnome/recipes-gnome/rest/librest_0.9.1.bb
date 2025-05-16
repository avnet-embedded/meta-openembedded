SUMMARY = "library to access web services that claim to be "RESTful""
HOMEPAGE = "https://wiki.gnome.org/Projects/Librest"
LICENSE = "LGPL-2.1-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=2d5025d4aa3495befef8f17206a5b0a1"

DEPENDS = " \
    glib-2.0 \
    glib-2.0-native \
    gtksourceview5 \
    libadwaita \
    libsoup \
    libxml2 \
    json-glib \
"

inherit gnomebase gobject-introspection vala pkgconfig gi-docgen

GNOMEBN = "rest"

SRC_URI[archive.sha256sum] = "9266a5c10ece383e193dfb7ffb07b509cc1f51521ab8dad76af96ed14212c2e3"

S = "${WORKDIR}/${GNOMEBN}-${PV}"

