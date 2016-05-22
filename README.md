RecyclerViewBaseAdapter相比BaseRecyclerViewAdapter有以下几点优化:  
1.泛型应用优化
---
原来的数据传递给子类时是没有带泛型的,需要子类自己去做强转,而这里我们已经通过泛型解决了些问题,子类在使用时不需要进行强转,直接使用.
2.填充数据优化
---
这里我们使用了一个带final修辞的list来作为数据源,在添加数据时,我们先去除重复数据,然后,再添加新数据,这个在做下拉刷新时,非常好用,我不
需要先清空data,而是直接添加数据就可以了.保证新添加的数据能被添加进来,而旧的已加载的数据,不需要再被重新加载.
3.错误提示优化
---
如果是做预加载出现错误或者当没有更多数据时的提示文字,都是滑动到最底部才会提示