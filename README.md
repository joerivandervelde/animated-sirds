# animated-sirds
A single-image random-dot stereogram (SIRDS) is a type of [autostereogram](https://en.wikipedia.org/wiki/Autostereogram).
In a SIRDS, a three-dimensional scene is contained in one image (where e.g. [stereograms](https://en.wikipedia.org/wiki/Stereoscopy) use two).
The image and depth information of the scene is encoded by a random dot pattern for each eye separately.
When viewed properly, the viewer experiences true three-dimensional vision and reveals the scene 'hidden' in the image.

## How to view SIRDS

Let's take a simple image of a sphere. The grayscale values act like a depth map, where white is closest and black is farthest.

![sphere-oneframe.gif](gifs/sphere-oneframe.gif)

We then convert the image into a SIRDS. There sphere can be seen again by letting the focal point of your eyesight move past the screen until the black dots overlap. Let your eyes slowly adjust to the image. If you are wearing eyeglasses, taking them off might make this much easier.

![sphere-oneframe-sirds.gif](gifs/sphere-oneframe-sirds.gif)

## Animating a SIRDS

We can animate the sphere by letting it move in a circular orbit. To achieve this in a simple way, we let the both the radius of the sphere and its depth (i.e. grayness) depend on the distance.

![a-sphere-r0.4-d0.1.gif](gifs/a-sphere-r0.4-d0.1.gif)

When converted to an animated SIRDS (ASIRDS), the result is a smoothly orbiting sphere with a convincing depth effect that is not difficult to follow.

![a-sphere-r0.4-d0.1-noisy.gif](gifs/a-sphere-r0.4-d0.1-noisy.gif)

## Question 1: reusing the pattern?

In the previous image, we generate a new random dot pattern for each frame.
This seems to be what people normally do for these type of images.
But is that necessary? What happens if we simply re-use the random dot pattern from the first frame, for all of the following frames?

We use the same starting image as before.

![a-sphere-r0.4-d0.1.gif](gifs/a-sphere-r0.4-d0.1.gif)

But now we re-use the pattern for each frame. The result is

![b-sphere-r0.4-d0.1-calm.gif](gifs/b-sphere-r0.4-d0.1-calm.gif)

## Question 2: no depth scaling?

![c-sphere-r0.4-d1.0.gif](gifs/c-sphere-r0.4-d1.0.gif)

![c-sphere-r0.4-d1.0-noisy.gif](gifs/c-sphere-r0.4-d1.0-noisy.gif)

## Question 3: no radius scaling?

![d-sphere-r1.0-d0.1.gif](gifs/d-sphere-r1.0-d0.1.gif)

![d-sphere-r1.0-d0.1-noisy.gif](gifs/d-sphere-r1.0-d0.1-noisy.gif)

## Conclusion

Convincing animated SIRDS must make use of 
