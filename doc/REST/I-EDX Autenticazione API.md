#Autenticazione API

Le API sono protette da un token non OTP, che _non viene negoziato_, e che quindi bisogna conoscere in anticipo.

Per eseguire una qualsiasi chiamata REST è necessario procedere in uno dei due modi seguenti:

- inserire un header con nome `i-edx-token` 
- inserire un query string un parametro `i-edx-token` 

In entrambi i casi i parametri devono avere come valore l'OTP.

es.

```
>http://127.0.0.1:8081/services/accesscode/api/devices?i-edx-token=554c8ffbe6a574c33680fb594ab8c0952b54d9301fc941a34ba27434cfd8cbcaa77
```
