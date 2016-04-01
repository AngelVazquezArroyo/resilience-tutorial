## Resilience Tutorial

This is the GitHub repository supporting a resilience tutorial that provides a step-by-step introduction to several basic resilience patterns.

NOTE: _Currently, the tutorial is not (yet) intended to be used as self-training without a trainer._

The exercises themselves including the required documentation are located in the corresponding `exercise_<number>_<description>` folders.

The folder infrastructure contains everything needed to set up the required infrastructure on a tutorial participant's computer.

Prerequisites:
* to be defined ...

## Getting The Tutorial Material

```
git clone https://github.com/ufried/resilience-tutorial.git
cd resilience-tutorial
```

## Working With The Training Material
In order to work with the tutorial material, you need to serve all files via an HTTP server. This can be any HTTP server which is capable of serving static assets. If you happen to have Python installed (which is installed by default on OS X and Linux), you can execute the following command to start an HTTP server in the current directory.

```
# with Python 2.x
python -m SimpleHTTPServer

# with Python 3.x
python -m http.server
```

## Testing

First start impostor engines:

* Upstream impostor
  $GOPATH/bin/impostor localhost:8100

* Downstream impostor
  $GOPATH/bin/impostor localhost:8102

