netsh wlan connect DIRECT-5m-14473-RC
adb disconnect
ping 127.0.0.1 -n 2 > nul
adb connect 192.168.49.1