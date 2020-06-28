set PATH=%PATH%;C:\Users\JavaDev1\Downloads\protoc-3.11.4-win64\bin
protoc -I=.\src --java_out=.\src .\src\com\caporal7\jroom\common\java\proto\*.proto
pause
