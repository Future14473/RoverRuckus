netsh wlan connect DIRECT-eC-14473-RC
adb disconnect
ping 127.0.0.1 -n 3 > nul
adb connect 192.168.49.1