# Under SELinux

The ``/usr/libexec/keepalived`` directory is automatically created and labeled with sufficient SELinux privileges. This directory is reserved for custom scripts created by users.

* /usr/libexec/keepalived/nginx-chk.sh
```bash
#!/bin/bash
/usr/bin/killall -0 nginx
exit $?     
```
* /etc/keepalived/keepalived.conf
  * specific vitual ip : 192.168.2.77
  * specific Ethernet DEVICE: ens160 (/etc/sysconfig/network-scripts/ifcfg-ens160)
)
```conf
global_defs {
   router_id  controller_1
}

vrrp_script  nginx {
#  script  "killall -0 nginx"
#  https://access.redhat.com/solutions/3880561
   script "/usr/libexec/keepalived/nginx-chk.sh"
   interval 2
   weight 2
}

vrrp_instance  50 {
  virtual_router_id 50
  advert_int 1
  priority 101
  state MASTER
  interface ens160
  virtual_ipaddress {
    192.168.2.77  dev  ens160
  }
  track_script {
    nginx
  }
}     
```
