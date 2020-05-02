# Word distribution tool!

Text tool that finds the most frequent phrases and words. The application provides concurrent file reader and its analysis based on requested parameters like word arity - parameter that specifies how many adjacent words will be grouped.

The tool is very well memory-optimized but on the other hand, memory mainly depends on the file size you want to analyze. 
To prevent Java Virtual Machine to exhausted its default heap memory you should set JVM parameter `-Xmx` to the desired memory limit.

Install all dependencies and start the application:
```sh
mvn clean install
mvn javafx:run
```
