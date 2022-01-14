# Determinants of convincing SIRDS animations

## SIRDS in a nutshell
[Stereograms](https://en.wikipedia.org/wiki/Stereoscopy) consist of two images that can merge to produce three-dimensional scenes.
This effect caused by providing differential depth information to your eyes.
In an [autostereogram](https://en.wikipedia.org/wiki/Autostereogram), a 3D scene is contained in one image by repeating narrow strips.
A single-image random-dot stereogram (SIRDS) is type of autostereogram in which these strips are random dot patterns.
At first glance, SIRDS resemble random static noise, but a hidden scene will be revealed when the image is viewed correctly.
SIRDS are usually still, but multiple images may be concatenated into an animated SIRDS (ASIRDS).
But what determines whether an ASIRDS looks good or not?
We can do some highly subjective experiments to perhaps get some clues.
But let's start at the beginning.

## How to view a SIRDS

Let's take a simple image of a sphere.
The grayscale values act like a depth map, where white is closest and black is farthest.

![sphere-oneframe.gif](gifs/sphere-oneframe.gif)

We then convert the image into a SIRDS.
There sphere can be seen again by letting the focal point of your eyesight move past the screen until the black dots overlap.
Let your eyes adjust to the depth effect of the image.
If you are wearing eyeglasses, taking them off might make this easier.

![sphere-oneframe-sirds.gif](gifs/sphere-oneframe-sirds.gif)

## Animating a SIRDS

We can animate the sphere by letting it move in a circular orbit.
To achieve this in a simple way, we let the both the radius of the sphere and its depth (i.e. grayness) depend on the distance.

![a-sphere-r0.4-d0.1.gif](gifs/a-sphere-r0.4-d0.1.gif)

When converted into an ASIRDS, the result is a smoothly orbiting sphere with a convincing depth effect that is easy to follow.
This will be our reference image.

![a-sphere-r0.4-d0.1-noisy.gif](gifs/a-sphere-r0.4-d0.1-noisy.gif)

## Experiment 1: reusing the pattern

In the reference image, we generate a new random dot pattern for each frame.
This seems to be the norm for these type of animations.
But is that necessary?
Let's use the reference image from before.

![a-sphere-r0.4-d0.1.gif](gifs/a-sphere-r0.4-d0.1.gif)

What happens when we simply re-use the random dot pattern from the first frame, for all following frames, when converting to an ASIRDS?
The result is a calmer image where the sphere can still be seen moving.
However, background echos from the stereoscopic encoding technique seem to make it nearly impossible to track the sphere in 3D.

![b-sphere-r0.4-d0.1-calm.gif](gifs/b-sphere-r0.4-d0.1-calm.gif)

## Experiment 2: no depth scaling

What happens when we disable the scaling of the grayscale depth values with distance?
The input image is similar to that of before, except the sphere is equally bright at all positions.

![c-sphere-r0.4-d1.0.gif](gifs/c-sphere-r0.4-d1.0.gif)

The result is at first glance similar to our reference image, but tracking the sphere with your eyes around its farthest point feels less natural and requires more effort.

![c-sphere-r0.4-d1.0-noisy.gif](gifs/c-sphere-r0.4-d1.0-noisy.gif)

## Experiment 3: no size scaling

What happens if we disable distance scaling of the sphere's radius but leave depth scaling untouched?
Does the sphere's movement still appear as circular despite the lack of shape information?
The input image would look like this:

![d-sphere-r1.0-d0.1.gif](gifs/d-sphere-r1.0-d0.1.gif)

When viewed as an ASIRDS, the animation is smooth and easy to follow.
However, instead of a circular orbit, the sphere seems to emerge from the background, move to the right, submerge into the background, and later re-appear on the left.

![d-sphere-r1.0-d0.1-noisy.gif](gifs/d-sphere-r1.0-d0.1-noisy.gif)

## Experiment 4: color vs monochrome

Would a colored ASIRDS be easier to view than a monochrome one?
Let's use our reference image as a starting point.

![a-sphere-r0.4-d0.1.gif](gifs/a-sphere-r0.4-d0.1.gif)

But instead of the monochrome color palette used so far, we use random RGB colors.
Compared to the reference ASIRDS, the colors seem to make the scene more smooth and subtle, but somehow also more bland.

![e-sphere-color-noisy.gif](gifs/e-sphere-color-noisy.gif)

## Experiment 5: low vs normal resolution

Finally, we can test if image resolution makes a difference in viewing experience.
Again we use the reference image.

![a-sphere-r0.4-d0.1.gif](gifs/a-sphere-r0.4-d0.1.gif)

This time, we lower the resolution by copying every other pixel to its neighbouring position.
The image width and height do not change, but one pixel is now the size of four, and three are lost.
While the sphere is trackable, the depth effect is surprisingly awkward to view, as if your eyes cannot focus properly on the object.

![f-sphere-lowres-noisy.gif](gifs/f-sphere-lowres-noisy.gif)

## Conclusions

A credible ASIRDS seems to depend on depth scaling proportional to object size, a fresh random dot pattern for each frame, and a high (or high enough) image resolution.
The used color palette does not seem to matter much.
These conclusions, based on a single input image and handful of opinions by one observer, should be regarded as speculation.
