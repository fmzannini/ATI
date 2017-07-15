#! /bin/bash

dir=$1
src_file=$2
extension=$3
basename=$(basename $src_file $extension)

convert $1/$2 -rotate 30 $1/$basename-rot30$extension
convert $1/$2 -rotate 45 $1/$basename-rot45$extension
convert $1/$2 -rotate 90 $1/$basename-rot90$extension
convert $1/$2 -rotate 180 $1/$basename-rot180$extension
convert $1/$2 -rotate 270 $1/$basename-rot270$extension

convert $1/$2 -resize 50% $1/$basename-resize50$extension
convert $1/$2 -resize 200% $1/$basename-resize200$extension

#convert $1/$2 -modulate 75,100,100 $1/$basename-light75$extension
#convert $1/$2 -modulate 150,100,100 $1/$basename-light125$extension
convert $1/$2 -brightness-contrast -25x0 $1/$basename-light75$extension
convert $1/$2 -brightness-contrast 25x0 $1/$basename-light125$extension

width=$(identify -format '%w' $1/$2)
height=$(identify -format '%h' $1/$2)

convert $1/$2 -matte -virtual-pixel white -distort Perspective "0,0 50,0  $width,0 $((width-50)),50  $width,$height $((width-50)),$((height-50))  0,$height 50,$height" $1/$basename-distX$extension
convert $1/$2 -matte -virtual-pixel white -distort Perspective "0,0 50,50  $width,0 $((width-50)),50  $width,$height $width,$((height-50))  0,$height 0,$((height-50))" $1/$basename-distY$extension
convert $1/$2 -matte -virtual-pixel white -distort Perspective "0,0 50,50  $width,0 $((width-50)),50  $width,$height $width,$((height-50))  0,$height 0,$height" $1/$basename-distXY$extension


#convert $1/$2 -distort AffineProjection 1,0,1,1,0,0 $1/$basename-rotY15$extension
#convert $1/$2 -distort AffineProjection 1,1,0,1,0,0 $1/$basename-rotX15$extension
#convert $1/$2 -shear 0x15 $1/$basename-rotY15$extension
#convert $1/$2 -shear 15x0 $1/$basename-rotX15$extension
#convert $1/$2 -shear 15x15 $1/$basename-rotXY15$extension

