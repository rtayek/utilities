#!/bin/sh

srcRoot=${1:-.}
dstDir=${2:-./collected}

srcRoot=/c/dfromrays8350/ray
dstDir=tmp/

mkdir -p "$dstDir"

find "$srcRoot" -type f -name "*til*.java"| while IFS= read -r f
do
    base=$(basename "$f")
    name=${base%.*}
    ext=${base##*.}

    target="$dstDir/$base"
    i=1

    while [ -e "$target" ]
    do
        target="$dstDir/${name}_$i.$ext"
        i=$((i+1))
    done

    cp "$f" "$target"
done
