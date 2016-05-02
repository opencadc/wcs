#################################################################
#                                                               #
# Sample makefile for a system running Linux                    #
# using Java and the GNU gcc compiler.                          #
#                                                               #
#################################################################
SHELL = /bin/sh
.SUFFIXES: .c .o

#################################################################
#                                                               #
# The following variables are specific to the build environment #
# and must be set to the appropriate values.                    #
#                                                               #
#################################################################

#
# Compiler with debugging.
#
CC = gcc -g

#
# Path to the WCSLIB install. The path to the WCSLIB install can 
# be overridden by passing make an alternate path:
# make wcs=/path_to_wcslib
#
wcs = /usr

#
# Path to the WCSLIB library.
#
WCSLIB = $(wcs)/lib64/libwcs.so

## find libwcs.so.4
WCSLIB = $(shell locate libwcs.so.4 | head -1)

#
# Directory containing the WCSLIB headers (but not including the wcslib/ subdir).
#
WCSLIB_HEADERS = $(wcs)/include

## find wcs/wcs.h
WCSH = $(shell locate wcs.h)
WCS_HEADERS = $(shell dirname $WCSH)
WCS_HEADERS = $(shell dirname $WCS_HEADERS)

## find location of JNI header
JNIH = $(shell locate jni.h | tail -1)
JNI_HEADERS = $(shell dirname $(JNIH))

JNIMD = $(shell locate jni_md.h | tail -1)
JNIMD_HEADERS = $(shell dirname $(JNIMD))

#
# Compiler flags.
#
FLAGS = \
-fPIC \
-D_REENTRANT \
-DCOMPILE_STYLE=ANSI_C_COMPILE \
-DCOMPILER_gnu \
-Dx86_linux \
-D_POSIX_C_SOURCE=2 \
-fexceptions \
-ansi \
-std=c99 \
-pedantic \
-Wall \
-Wno-parentheses \
-Wno-long-long \
-Wimplicit

#################################################################
#                                                               #
# The remaining variables are not system specific. The default  #
# values should suffice, but can be modified if desired.        #
#                                                               #
#################################################################

#
# Source file.
#
SRC = src/wcslib.c
OBJ = build/tmp

#
# WcsLIB javah generated header.
#
#WCSLIB_HEADER = h/wcslib.h
WCSLIB_JNI_HEADER = build/tmp/ca_nrc_cadc_wcs_WCSLib.h

#
# Object file.
#
OBJECTS = $(OBJ)/wcslib.o

#
# Shared library file.
#
SHARED = $(OBJ)/libwcsLibJNI.so

#
# Include directories for compiling
#
INCLUDE_PATH = -I$(JNI_HEADERS) -I$(JNIMD_HEADERS) -I$(WCSLIB_HEADERS)

CFLAGS = \
$(FLAGS) \
$(INCLUDE_PATH)

#################################################################
#                                                               #
# Make targets                                                  #
#                                                               #
#################################################################
.PHONY: all
all:    mkdirs $(SHARED)

.PHONY:	mkdirs
mkdirs:	clean
	-mkdir $(OBJ)

$(SHARED): $(OBJECTS)
	$(CC) -shared $(CFLAGS) $(OBJECTS) $(WCSLIB) -o $(SHARED)

$(OBJECTS) : $(SRC) $(WCSLIB_HEADER)
	$(CC) -c $(CFLAGS) $(SRC) -o $(OBJECTS)

.PHONY : clean
clean :
	-rm -fr $(OBJ) $(DEST)
