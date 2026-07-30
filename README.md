# Clojure Graphic Animation

![Clojure](https://img.shields.io/badge/Clojure-1.11.1-5881D8?logo=clojure&logoColor=white)
![Leiningen](https://img.shields.io/badge/Leiningen-Build-2C3E50)
![Seesaw](https://img.shields.io/badge/GUI-Seesaw-6A5ACD)
![Status](https://img.shields.io/badge/Status-Academic%20Project-success)

A functional graphics application written in **Clojure** that generates pixel-based animations by interpreting a small user-defined language through a custom virtual machine.

The application produces an animation composed of **256 frames**, with each frame rendered as a **256 × 256 RGB image**. Every pixel is calculated by evaluating the user program against its coordinates, the current frame, and the virtual machine state.

This project was co-developed by **Martino De Ninis** and **Agustín Sauer** for the Programming Paradigms course at the Faculty of Engineering of the University of Buenos Aires.

<p align="center">
    <img
      src="https://img.youtube.com/vi/7fGdf1nu11U/maxresdefault.jpg"
      alt="Clojure Graphic Animation demo"
      width="720"
    >
  </a>
</p>



## Overview

The application allows users to enter a program written in a small domain-specific language.

That program is evaluated for each pixel of each animation frame. Its output determines the red, green, and blue components of the resulting image.

The rendering pipeline can be summarized as follows:

1. The user enters a program.
2. The program is parsed into an internal representation.
3. A virtual machine evaluates the program for each pixel.
4. The resulting RGB values are converted into an image.
5. Generated frames are displayed sequentially as an animation.

## Core Concepts

### Functional programming

The application was developed using functional programming principles such as:

- Immutable data structures
- Pure functions
- Function composition
- Transformation of collections
- Explicit state transitions
- Separation between computation and side effects

### Virtual machine

The rendering engine uses a custom virtual machine to interpret the program supplied by the user.

For each pixel, the virtual machine receives the values required to calculate its color and returns an updated execution state together with the resulting RGB components.

The virtual machine logic is kept separate from the graphical interface, allowing the interpreter and rendering behavior to be reasoned about independently.

### Pixel rendering

Each generated frame contains:

```text
256 × 256 = 65,536 pixels
```

A complete 256-frame animation requires evaluating:

```text
256 × 256 × 256 = 16,777,216 pixels
```

Each pixel is represented through three RGB components with values between `0` and `255`.

### Graphical interface

The desktop interface allows users to:

- Enter or edit the animation program
- Start the rendering process
- View generated frames
- Play the resulting animation
- Observe rendering or execution progress
- Handle invalid programs or execution errors

## Features

- Custom user-programmable animation language
- Functional virtual machine
- Per-pixel RGB computation
- Generation of 256 animation frames
- 256 × 256 pixel image resolution
- Desktop graphical interface
- Animation playback
- Separation between interpretation, rendering, scheduling, and presentation
- Command-line and graphical execution modes
- Collaborative development with Git and GitHub

## Tech Stack

- **Clojure 1.11.1**
- **Leiningen**
- **Seesaw**
- **Java Virtual Machine**
- **Functional Programming**
- **Git**
- **GitHub**

## Architecture

The application is organized around several responsibilities.

### Language processing

Responsible for converting the source code entered by the user into a representation that can be evaluated by the virtual machine.

Depending on the final implementation, this layer may include:

- Tokenization
- Parsing
- Syntax validation
- Instruction or expression construction
- Error reporting

### Virtual machine

Responsible for:

- Evaluating the parsed program
- Maintaining the explicit execution state
- Applying language operations
- Producing RGB values
- Returning updated state without relying on uncontrolled mutation

### Rendering engine

Responsible for:

- Traversing each frame
- Visiting every pixel coordinate
- Invoking the virtual machine
- Normalizing RGB values
- Creating the rendered image
- Building the final frame sequence

### Scheduler

Responsible for coordinating frame generation and animation playback without placing timing logic inside the domain operations.

### Observer and presentation layer

The graphical interface observes the relevant application state and displays:

- Current rendering progress
- Generated frames
- Animation playback
- Validation or execution errors

This separation prevents the graphical interface from defining the interpretation and rendering rules.

## Project Structure

Update this tree if the final repository uses different namespaces.

```text
.
├── project.clj
├── README.md
├── resources/
├── src/
│   └── tp2_anim/
│       ├── core.clj
│       ├── language/
│       ├── parser/
│       ├── vm/
│       ├── renderer/
│       ├── scheduler/
│       └── ui/
└── test/
    └── tp2_anim/
```

## Requirements

Before running the project, install:

- Java Development Kit
- Clojure
- Leiningen
- Git

Verify the installation:

```bash
java --version
clojure --version
lein --version
git --version
```

## Installation

Clone the public repository:

```bash
git clone https://github.com/Dnmartinoo/Clojure-Graphic-Animation.git
cd Clojure-Graphic-Animation
```

Install the project dependencies:

```bash
lein deps
```

## Running the Application

### Graphical interface

Run the application without command-line arguments:

```bash
lein run
```

### Command-line program

When supported by the final implementation, a program can be passed directly:

```bash
lein run "<program>"
```

Because the animation language has its own syntax, surround the program with quotes when it contains spaces or shell-sensitive characters.

## Testing

Run the automated test suite with:

```bash
lein test
```

The test suite should cover the most important domain behavior, including:

- Parser validation
- Individual language operations
- Virtual machine state transitions
- RGB value normalization
- Pixel evaluation
- Frame generation
- Invalid program handling
- Rendering edge cases

## Building a Standalone JAR

Create an executable standalone package:

```bash
lein uberjar
```

The generated file will be available inside the `target/uberjar/` directory.

Run it with:

```bash
java -jar target/uberjar/clojure-graphic-animation-1.0.0-standalone.jar
```

## Development Process

The project was developed collaboratively by a two-person team.

Both contributors shared responsibilities across:

- Domain modeling
- Functional implementation
- Virtual machine development
- Graphical interface integration
- Debugging
- Testing
- Refactoring
- Git branch integration
- Merge conflict resolution
- Documentation

Git and GitHub were used for version control and collaborative development.

## Design Goals

The main goals of the project were:

- Applying functional programming to a non-trivial problem
- Keeping state transitions explicit
- Isolating graphical side effects from domain logic
- Interpreting a custom language
- Processing a large number of pixel evaluations
- Coordinating computation and animation playback
- Building a maintainable graphical application in Clojure

## Performance Considerations

A full animation evaluates more than sixteen million pixels.

This makes the following aspects particularly important:

- Avoiding unnecessary intermediate collections
- Keeping pixel operations small and predictable
- Separating rendering from interface updates
- Preventing graphical operations from blocking the application unnecessarily
- Reusing immutable program representations
- Measuring performance before introducing optimizations

No specific performance guarantees are claimed unless supported by benchmarks.

## Project Status

The academic implementation is complete.

Potential future improvements include:

- Improved syntax highlighting
- More descriptive parser and runtime errors
- Exporting the animation as GIF or video
- Configurable image dimensions
- Configurable frame count
- Rendering progress indicators
- Additional language operations
- Performance benchmarks
- Continuous integration with GitHub Actions
- Downloadable releases

## Academic Context

This project was originally developed in a private GitHub organization for the Programming Paradigms course at FIUBA.

The public repository was created with authorization for portfolio and professional presentation purposes.

## Authors

### Martino De Ninis

Computer Engineering student at the University of Buenos Aires.

- GitHub: [Dnmartinoo](https://github.com/Dnmartinoo)

## Disclaimer

This repository is published for educational and portfolio purposes.

The application and its source code were developed by the listed contributors as part of an academic assignment.
