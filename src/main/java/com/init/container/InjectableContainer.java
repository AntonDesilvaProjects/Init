package com.init.container;

/**
 *  This class is an abstraction of the "platform" on which the application
 *  runs. It should be able to support:
 *      1. Creation and management of the class instances along with rapid
 *      access to instances. Creating instance
 *      2. Actual injection of instances via reflection
 *      3. Container lifecycle which would fire events
 *
 *      a. Scan and build a map of annotated classes.
 *      b. Build dependency graph
 *      c. Use dependency graph to create and store instances
 *      d. start the application.
 *
 *
 * */
public class InjectableContainer {

}
