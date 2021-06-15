# CADC WCS 2.1.0

The `cadc-wcs` library is the java interface to `WCSLib` (http://www.atnf.csiro.au/people/mcalabre/WCS/).

This library can be built on its own but testing and beyond requires `WCSLib`. The JNI binding library is
compiled and included inside the `cadc-wcs` JAR file and deployed during class loading.

## Supported WCSLib versions

Beginning with version 2.1.0, the JNI binaries are linked against `WCSLib` major versions 5, 6, or 7 (which ever is found).  In the
`src/main/resources` directory we've included JNI liked libraries.  To add support for a different version, simply build
against that version of `WCSLib`, then copy the `libwcsLibJNI.so` to `src/main/resources/libwcsLibJNI.<version>.so` and it
will be picked up.  To remove support, simply delete that `src/main/resources/libwcsLibJNI.<version>.so` file.

## Building & Testing

JDK 1.8 (or higher) is required.  The Gradle Wrapper is provided.

### WCSLib

### No JNI changes

To use `cadc-wcs`, the `WCSLib` C library is required.  If it is not installed, you may skip the tests:

 1. `$> ./gradlew -i -x test clean build` -- build the JAR file if no `WCSLib` is installed.

Or in an environment where `WCSLib` with a supported version (currently 5.x, 6.x, or 7.x) is installed:

 1. `$> ./gradlew -i clean build test` -- build the JAR file if no `WCSLib` is installed.

### With JNI changes

The `WCSLib` C library is required.

1. `$> ./gradlew -i -b build-jni.gradle -x test clean build && ./gradlew -i assembleSharedJar` -- build the JAR file if no `WCSLib` is installed.

Or in an environment where `WCSLib`with a supported version (currently 5.x, 6.x, or 7.x) is installed:

1. `$> ./gradlew -i clean build test` -- build the JAR file if no `WCSLib` is installed.
