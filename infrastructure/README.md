R.E.D. for the resilience tutorial
==================================

R.E.D. means "Runtime Environment for Developers". It creates a Vagrant powered virtual machine containing everything a resilience tutorial user needs to run the tutorial software on a developer notebook.

Prerequisites
-------------

You need to have installed the following software (all in a recent version) on your machine in order to run the given scripts:

* Virtualbox
* Vagrant
* Ansible

Installation
------------

Just issue `vagrant up` in the base directory of this project.

_Note_: You might want to check the `Vagrantfile` for its CPU and memory settings of the VM it creates. Currently if goes for 4 CPUs and 6 GB of memory. If your developer machine is not capable to handle this amount of CPU or memory (or if you even want to provide more resources), you should tweak those settings before running `vagrant up`. Please make sure that you are not going to check in a version of `Vagrantfile` with tweaked settings. (At a later point in time the tweaking might be changed to an environment variable approach to make handling easier. But that will be then ...)
