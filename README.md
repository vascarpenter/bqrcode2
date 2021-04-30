# BQRCodeApp2

- QRcode Maker run under Java6 SE environment
  - using older version of zxing library `core-java6-3.2.0.jar` and `javase-java6-3.2.0.jar` needed
- written in kotlin & java(Swing GUI) for IntelliJ
  - set your Project Structure > Project Language Level > 1.6
  - set your Preferences > Build,Execution,Deploy > Compiler > Kotlin Compiler > Target JVM Version > 1.6
- Press "open file.." button to make its content to QR code
- large file (more than 2000bytes) will be divieded in multiple QR codes.
  - Use `<<` and `>>` button for viewing prev and next image.  
- base64 encoded
- If you want to decode it, try `echo XXXX== (or pbpaste) | base64 --decode` in your unix shell.
