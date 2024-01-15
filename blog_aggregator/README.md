<a href="https://codeclimate.com/github/TuPi4Ok/test_tasks/maintainability"><img src="https://api.codeclimate.com/v1/badges/a2cd67b3d2d4b317e506/maintainability" /></a>
## blog_aggregator
blog_aggregator is rss feed aggregation application. 
The result of its work is *output.html* file, which contains links to 50 latest posts of aggregated resources.
## Resources
The [*rss-resources.xml*](./src/main/resources/rss-resources.xml) file stores rss feeds. To add a feed to the aggregation you need to add an item tag with a link to the resource.  
**Example:**
```xml
<resources>
<item>http://rss.cnn.com/rss/cnn_topstories.rss</item>
<item>https://rss.nytimes.com/services/xml/rss/nyt/HomePage.xml</item>
<item>https://www.theguardian.com/world/rss</item>
<item>https://feeds.feedburner.com/TechCrunch</item>
<item>https://www.reddit.com/r/technology/.rss</item>
<item>https://www.wired.com/feed/</item>
<item>https://news.ycombinator.com/rss</item>
</resources>
```
