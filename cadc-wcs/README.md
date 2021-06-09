# CADC WCS 2.1.0

The `cadc-wcs` library is the java interface to `WCSLib` (http://www.atnf.csiro.au/people/mcalabre/WCS/).

This library can be built on its own but testing and beyond requires `WCSLib`. The JNI binding library is
compiled and included inside the `cadc-wcs` JAR file and deployed during class loading.

Versions: 
- `cadc-wcs` >= 2.1.0 binaries on Maven Central are linked against `WCSLib` major versions 5, 6, or 7 (which ever is found). 

## Building & Testing

JDK 1.8 (or higher) is required.  The Gradle Wrapper is provided.

### WCSLib

To use `cadc-wcs`, the `WCSLib` C library is required.  If it is not installed, you may skip the tests:

 1. `$> ./gradlew -i -x test clean build` -- build the JAR file if no `WCSLib` is installed.

Or in an environment where `WCSLib` 5.x, 6.x, or 7.x is installed:

 1. `$> ./gradlew -i clean build test` -- build the JAR file if no `WCSLib` is installed.
