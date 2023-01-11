# CSC365_A2

This represents some of my work in 
CSC 365 - Data Structures and Algorithms
At SUNY Oswego

This extends Assignment 1 using persistent data structures and additional similarity analyses. It requires two programs.

Loader:

 - For each of at least 100 URLs, create a persistent file-based B-Tree or Hash Table with word frequencies. (You may choose to store only a high-quality hash code rather than the word itself). Give each file a name based on its URL in order to locate it easily.
 
 - Load each map with frequencies (possibly along with other data) extending or changing those in assignment 1 if applicable.
  
 - Implement and use a fixed size buffer cache to reduce IO. Pre-categorize pages into 5 to 10 clusters using k-means, k-mediods, or a similar metric.
  
Application:

- Extend Assignment 1 to display a category (cluster) and most similar site from the above data structures.

