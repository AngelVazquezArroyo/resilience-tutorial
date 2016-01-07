docker
======

Installs Docker

Requirements
------------

This role requires Ansible 1.4 or higher.

Role Variables
--------------

| Name                  | Default | Description                                     |
|-----------------------|---------|-------------------------------------------------|
| docker_distro_version | trusty  | Ubuntu distribution for which to install Docker |

Dependencies
------------

None

Example Playbook
----------------

Install Docker
```
- hosts: all
  roles:
    - docker
```

Install Docker for Ubuntu vivid
```
- hosts: all
  roles:
    - { role: docker, docker_distro_version: vivid }
```
