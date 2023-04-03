# cadc-erfa-1.0

The `cadc-erfa` library is the java interface to `ERFA (Essential Routines for Fundamental Astronomy)` 
(https://github.com/liberfa/erfa).

This library can be built on its own but testing and beyond requires `liberfa`, the ERFA C library. 
The JNI binding library is compiled and included inside the `cadc-erfa` JAR file and deployed during class loading.

## Supported ERFA versions

The JNI binary is linked against `ERFA` version 2.0.0. The `src/main/resources` directory includes the JNI linked library.
To add support for a different version, build against that version of `ERFA`, and copy the `liberfaLibJNI.so` 
to `src/main/resources/`.

The src tree now contains a build of `liberfaLibJNI.dylib` for MacOS. This is expected to be useful to make life easier for
developers working on code that uses this library and is known to work on 12.x (at least).

## Building & Testing

JDK 1.8 (or higher) is required.  The Gradle Wrapper is provided.

### ERFA

### No JNI changes

To use `cadc-erfa`, the `ERFA` C library is required.  If it is not installed, skip running the unit tests using the `-x test` option:

 1. `$> ./gradlew -i -x test clean build` -- build the JAR file and do not run the unit tests.

Or in an environment where `ERFA` with a supported version (currently 2.0.0) is installed:

 1. `$> ./gradlew -i clean build test` -- build the JAR file and run the unit tests.

### With JNI changes

If JNI changes are made to `erfalib.c`, use the `build-jni.gradle` build file. 

The `ERFA` C library is required. If it is not installed, skip running the unit tests using the `-x test` option:

1. `$> ./gradlew -i -b build-jni.gradle -x test clean build && ./gradlew -i build` -- build the JAR file 
and do not run the unit tests.

Or in an environment where `ERFA` with a supported version (currently 2.0.0) is installed:

1. `$> ./gradlew -i -b build-jni.gradle clean build test && ./gradlew -i build` -- build the JAR file and run the unit tests.
