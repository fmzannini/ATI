#! /bin/bash

for file in $(ls $1)
do
	echo "$1/$file \n"
	if [ -f $1/$file ]
	then
		echo "convert $1/$file $1/$(basename $file .$2).$3 \n"
		convert $1/$file $1/$(basename $file .$2).$3
	fi
done

