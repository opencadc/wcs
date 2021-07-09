# CADC WCS 2.1.0

The `cadc-wcs` library is the java interface to `WCSLib` (http://www.atnf.csiro.au/people/mcalabre/WCS/).

This library can be built on its own but testing and beyond requires `WCSLib`. The JNI binding library is
compiled and included inside the `cadc-wcs` JAR file and deployed during class loading.

## Supported WCSLib versions

Beginning with version 2.1.0, the JNI binaries are linked against `WCSLib` major versions 5, 6, or 7 (which ever is found).  The
`src/main/resources` directory includes JNI linked libraries.  To add support for a different version, build
against that version of `WCSLib`, copy the `libwcsLibJNI.so` to `src/main/resources/libwcsLibJNI.<version>.so` and add 
it to the array in `WCSLib.java`.  To remove support, delete the `src/main/resources/libwcsLibJNI.<version>.so` file from
both the `src/main/resources` directory and the `WCSLib.java` file.

## Building & Testing

JDK 1.8 (or higher) is required.  The Gradle Wrapper is provided.

### WCSLib

### No JNI changes

To use `cadc-wcs`, the `WCSLib` C library is required.  If it is not installed, skip running the unit tests using the `-x test` option:

 1. `$> ./gradlew -i -x test clean build` -- build the JAR file and do not run the unit tests.

Or in an environment where `WCSLib` with a supported version (currently 5.x, 6.x, or 7.x) is installed:

 1. `$> ./gradlew -i clean build test` -- build the JAR file and run the unit tests.

### With JNI changes

If JNI changes are made to wcslib.c, use the `build-jni.gradle` build file. 

The `WCSLib` C library is required. If it is not installed, skip running the unit tests using the `-x test` option:

1. `$> ./gradlew -i -b build-jni.gradle -x test clean build && ./gradlew -i build` -- build the JAR file 
and do not run the unit tests.

Or in an environment where `WCSLib`with a supported version (currently 5.x, 6.x, or 7.x) is installed:

1. `$> ./gradlew -i -b build-jni.gradle clean build test && ./gradlew -i build` -- build the JAR file and run the unit tests.
