nomad
=====

Installs Nomad.

Note that this role will not start Nomad. To start Nomad you need to log into the given node and issue the command `nomad agent` with the desired parameters (e.g., for development mode this would be `nomad agent -dev`).

Requirements
------------

This role requires Ansible 1.8 or higher.

Role Variables
--------------

| Name              | Default           | Description                                            |
|-------------------|-------------------|--------------------------------------------------------|
| nomad_version     | 0.2.3             | Version of Nomad to install                            |
| nomad_archive_dir | /vagrant/archives | The location where to store the downloaded archive     |
| nomad_install_dir | /usr/bin          | The directory where the Nomad binary will be installed |

Dependencies
------------

None

Example Playbook
----------------

Install Nomad
```
- hosts: all
  roles:
    - nomad
```

Install Nomad Version 0.1.2
```
- hosts: all
  roles:
    - { role: nomad, nomad_version: 0.1.2 }
```

Install Nomad downloading archive to /usr/src
```
- hosts: all
  roles:
    - { role: nomad, nomad_archive_dir: /usr/src }
```

Install Nomad in /usr/local/bin
```
- hosts: all
  roles:
    - { role: nomad, nomad_install_dir: /usr/local/bin }
```
