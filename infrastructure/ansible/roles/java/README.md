java
====

Installs Java

Credits
-------

Derived from the kbrebanov.java role available in Ansible Galaxy.
See https://github.com/kbrebanov/ansible-java for more details.

Requirements
------------

This role requires Ansible 1.4 or higher.

Role Variables
--------------

| Name                    | Default | Description                                           |
|-------------------------|---------|-------------------------------------------------------|
| java_version            | 8       | Version of Oracle Java to install                     |
| java_install_jce_policy | false   | Enable/Disable installation of Oracle Java JCE policy |

Dependencies
------------

None

Example Playbook
----------------

Install Oracle Java 8
```
- hosts: all
  roles:
    - java
```

Install Oracle Java 8 and JCE policy
```
- hosts: all
  roles:
    - { role: java, java_install_jce_policy: true }
```

Install Oracle Java 7
```
- hosts: all
  roles:
    - { role: java, java_version: 7 }
```
