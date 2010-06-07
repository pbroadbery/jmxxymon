Simple JMX monitor for xymon

Allows monitoring of java programs (via jmx) by xymon.
Supports:
	user-specified tests
	multiple hosts etc

Configuration:

The jar file (xymon_jmx.jar) and wrapper script (jmx_wrapper.sh) should be on xymon's client search path.
The script should be called from hobbitlaunch.cfg.  It needs no additional parameters.

Configuration:
For each host that needs testing, add a file
client/etc/jmx/host.conf, and add jmx=jmx/host.conf into the bb-hosts file.

File format:

The test configuration file is a line based file.  Any line starting
with '#' is assumed to be a comment and ignored.  Each line defines a single test; the fields are:
testName: Name of the test (column name on xymon pages)
line name 
port number
jmx subsystem name
jmx object name
jmx attribute name
test

For example:
testName,loadAvg,1099,java.lang,OperatingSystem,SystemLoadAverage,lessThan(10)

Will test the system load average as reported by jmx, reporting green
if less than 10, red otherwise.  The details page will show 'loadAvg:
xxx' where xxx is the reported load average.

The jmx subsystem name can be omitted if the object name is given in full; so for example

testName,SystemLoadAverage,1099,,java.lang:type=OperatingSystem,SystemLoadAverage,lessThan(10)

is equivalent to the above.  This is useful when the object name isn't
of the form subsytem:xxx=objname, where xxx is one of name, type and
service.  JConsole shows the object name on the MBeanInfo page for
each object.

Tests:

A test expression can be any of:

an id naming a test - eg. notNull
an id naming a parameterised test with a comma separated list of test expressions or literals - eg. between(0, 5)
A literal is a number or string (double quote delimited).

The following tests are supported:

notNull: This is the default; any jmx failures will be taken as a test failure.

greaterThan(v1): green if the value is greater than v1, red otherwise

lessThan(v1): green if the value is less than v1, red otherwise

equal(s): green if the value is equal to s, red otherwise (this uses
	  toString internally, so both numbers and strings will work).

range(low,lownotify,highnotify,high): 
   Red if value < low or > high, green if value >= lownotify and value
     <= highnotify, yellow otherwise range(low,high): Red if value <
     low or > high, green if value > low and value < high

shouldBe(x): If the result of the test x is red or yellow, then yellow otherwise green.  Handy for warnings.

and(t1,...tn): Green if all tests are green, red if any test is red, yellow otherwise
or(t1,...tn): Green if any test is green, red if all tests are red, yellow otherwise

Additional tests are fairly easy to add - see source code.
