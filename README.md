JEnumerable
===========

JEnumerable is an attempt at a Java implementation of the system.linq.Enumerable class 
from .NET 3.5+

I am implementing this alongside reading Jon Skeet's blog series 'Reimplimenting LINQ to 
Objects' therefore there are parallels with Jon's code especially in the test cases. The 
start of the blog series can be found at [Jon's MVP blog][reimpllinq]

[reimpllinq]: http://msmvps.com/blogs/jon_skeet/archive/2010/09/03/reimplementing-linq-to-objects-part-1-introduction.aspx 

JEnumerable was created to help me learn more about the LINQ methods and to simply see if 
it is possible to be implemented in Java in a somewhat usable form. It may not have much 
use beyond that. If you see a potential application for it however please go ahead.


Getting Started
---------------

As we don't have extension methods in Java, JEnumerable wraps a 'source' Iterable object:

	List<MyObj> myList = new ArrayList<MyObj>();
	... snip population of myList ...
	JEnumerable<MyObj> enumerableList = new JEnumerable<MyObj>(myList);
	
Once you have a JEnumerable it supports the same methods as the .NET LINQ library. Where 
in .NET you typically use lambda functions, in JEnumerable there are a set of abstract 
classes to perform the relevant operations through an anonymous subclass. For example given the 
following .NET statement:

	// Assuming integerList is an IEnumerable<int>
	var stringsList = integerList.Select(source => source.ToString());
	
The same can be achieved in Java through JEnumerable with the following code: 

	// Assuming integerList is a JEnumerable<Integer>
	JEnumerable<String> stringsList = 
		integerList.select(new Translator<Integer, String>() {
			@Override
			public String translate(final Integer source) {
				return source.toString();
			}
		});
				
		
License
-------
Please read the LICENSE file
