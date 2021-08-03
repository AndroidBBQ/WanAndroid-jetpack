## WanAndroid+kotlin+jetpack+mvvm+协程+buildSrc
### 先上图

<figure class="four">
    <img src="https://i.loli.net/2021/08/02/onSt58elXI46z7r.jpg" width="180px">
    <img src="https://i.loli.net/2021/08/02/37VZcPj1FyiQvwA.jpg" width="180px">
    <img src="https://i.loli.net/2021/08/02/eY63wjiAHdgUNGC.jpg" width="180px">
    <img src="https://i.loli.net/2021/08/02/bPnIDmVRt5eiL2a.jpg" width="180px">
</figure>
### 下载体验

[apk](https://github.com/AndroidBBQ/WanAndroid-jetpack/blob/main/app-debug.apk)

### 简介
这是一个本人为了学习、巩固kotlin、mvvm等一些流行框架而写的，里面的一些布局借用了其他项目。

* 项目的全部使用的都是kotlin语言，架构采用了MVVM架构，模块化开发（模块化和组件化差别不大，模块化后只要稍微改点东西就变成了组件化了），
* jetpack中使用了lifecycle、livedata、viewModel、paging3、navigation、room等。
* 注解框架使用的是koin，这个和mvvm配合使用个人觉得很巴适。
* 网络框架使用的是okhttp+retrofit+协程，然后再经过简单的封装，使用起来很方便。
* 项目的统一依赖使用的是buildSrc，这个应该是趋势了，可以看google的最新框架的统一依赖用的都是它。
* 文章的预览webview使用的是[AgentWebView](https://github.com/Justson/AgentWeb)
* recycler的adapter使用的是[BRVAH](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)，最新的3.X的版本对mvvm支持的挺好的。
* 请求的错误页面，我使用了loadsir和BRVAH两种方式，毕竟是学习嘛～
* 路由跳转使用的是Aroute，主要实现通过IProvider实现接口

项目结构
![](https://i.loli.net/2021/08/03/ObZr91CDWPAYUof.png)

app是入口模块，中间一层是业务模块，下面一层是底层模块和业务无关。

### 项目的统一依赖使用的是buildSrc
该目录buildSrc被视为包含的构建。发现该目录后，Gradle 会自动编译和测试此代码，并将其放入构建脚本的类路径中。对于多项目构建，只能有一个buildSrc目录，该目录必须位于项目根目录中。 buildSrc应该优先于脚本插件，因为它更容易维护、重构和测试代码。

关于buidlSrc的使用，可以参考 https://github.com/AndroidBBQ/CommonVersion

### 模块化和组件化
模块化和组件化其实是一样的,组件化就是比模块化多了个入口，该项目使用的是模块化，我把其中的导航模块给组件化了。

组件化：
1. 控制该组件化是模块还是application，比如通过 gradle.properties
```groovy
isBuildModule=false
```
2. 在模块的build.gradle下，控制是模块还是application
```groovy
if (isBuildModule.toBoolean()) {
    //作为独立App应用运行
    apply plugin: 'com.android.application'
} else {
    //作为组件运行
    apply plugin: 'com.android.library'
}
```
3. 配置清单文件,位置需要复制一份当前的清单文件到main/alone(可以随便取)目录下
```groovy
android{
    sourceSets {
        main {
            jniLibs.srcDirs = ['libs']
            if (isBuildModule.toBoolean()) {
                //独立运行
                manifest.srcFile 'src/main/alone/AndroidManifest.xml'
            } else {
                //合并到宿主
                manifest.srcFile 'src/main/AndroidManifest.xml'
                resources {
                    //正式版本时，排除alone文件夹下所有调试文件
                    exclude 'src/main/alone/*'
                }
            }
        }
    }
}
```
4. 在清单文件中提供入口,这个入口要注意点，假如我是可以直接进入的不需要做什么初始化操作的，我可以直接在activity中放个fragment展示出来。
如果需要一些信息的，然后在activity中先做些必要的初始化操作后再将页面放入。
```xml
<application>
        <activity android:name=".ui.AloneActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
</application>
```

### 依赖注入框架koin
[Koin](https://insert-koin.io/docs/setup/v3) 是纯 Kotlin 编写的轻量级依赖注入框架，轻量是因为它只使用 Kotlin 的函数解析特性，没有代理，没有代码生成，没有反射！官方声称5分钟快速上手。随着 Kotlin 的推广，Koin 这个后起之秀也获得了越来越多的关注。

koin使用起来真的挺好用的，而且对比hint和dagger更加简单，性能也更高，可以了解下。我这个项目用它主要是为了让mvvm使用起来更加方便。

比如我的navigation模块中需要用到NavTabVM
1。application中先注册
```kotlin
startKoin {
            androidContext(this@WanApp)
            modules(appModule)
        }
```
2. 这个appModule
```kotlin
val appModule = immutableListOf(otherModule, homeModels, webModels, loginModel, navModel, userModel)
```
3. navModel放在navigation中
```kotlin
val navModel = module {
    single {
        RetrofitManager.instance.create(NavApi::class.java)
    }

    single {
        NavRepo(get())
    }

    viewModel {
        NavTabVM(androidApplication(), get())
    }
}
```
4. 如果有Activity或者Fragment要使用的话，可以直接下面代码就引用了，koin会自动关联对应的生命周期，其他的我们都不用管。
其实通过这样也就实现了vm和v的解耦了，而且也不用关心vm的构造方法的传值了。
```kotlin
private val viewModel: NavTabVM by viewModel()
```

### mvvm

项目的mvvm遵循这个架构图
![](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/8b1974ac11964a0598e20a66defb07e6~tplv-k3u1fbpfcp-watermark.image)

我的BaseActivity设计的是下面这样的，是因为我觉得View层和databinding先关联就行了，如果说这个这个activity只用非常简单的功能的话，就不需要给它配置vm了。不能为了mvvm而加重程序本来的负担。
如果在需要vm的activity中完全可以通过上面说的 koin 的注解直接引用。
```kotlin
abstract class BaseVMActivity<T : ViewDataBinding> : AppCompatActivity() {
    lateinit var mBinding: T
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, getLayoutId())
        mBinding.lifecycleOwner = this
        initView(savedInstanceState)
        initData()
        startObserver()
    }
}
```
vm只是作为数据的中转，由于viewModel的特性viewModel只是作为数据的中转，它里面和V层的数据交互主要还是用到了LiveData，交互模式就是下面这种。
![](https://upload-images.jianshu.io/upload_images/19956127-45660edb30e786bb.png?imageMogr2/auto-orient/strip|imageView2/2/w/1200/format/webp)

不管这个数据是本地的还是从网络上请求的，数据都应该放到Response中进行处理，我这里只用了网络请求，所以只用repo提供了网络数据，提供本地数据操作一样。

这里就以获取公众号列表为例子：

在View层，通过viewModel的获取数据，并监听viewModel获取到数据livedata
```kotlin
class PublicTabFragment : BaseVMFragment<NavFragmentPublicTabBinding>() {
    //使用koin关联viewModel
    private val viewModel: NavTabVM by viewModel()
    //通过viewModel请求公众号列表数据
    override fun initData() {
        super.initData()
        viewModel.getWeChatList()
    }
    override fun startObserver() {
        super.startObserver()
        //监听viewModel请求数据
        viewModel.mWeChatList.observe(this, {
            when (it.dataStatus) {
                //请求中
                DataStatus.STATE_LOADING -> {
                    //展示加载框
                    showLoading()
                }
                DataStatus.STATE_ERROR -> {
                    //发生错误，关闭加载框，展示错误信息
                    dismissLoading()
                    it?.exception?.msg?.showToast()
                }
                DataStatus.STATE_SUCCESS -> {
                    //加载成功，关闭加载框
                    dismissLoading()
                    if (it.data.isNullOrEmpty()) {
                        toast("数据为空!")
                        return@observe
                    }
                    mLeftList.clear()
                    //默认选中第一个
                    it.data!![0].isSelected = true
                    mCurrentNavTab = it.data!![0]
                    //加入list
                    mLeftList.addAll(it.data!!)
                    mLeftAdapter.notifyDataSetChanged()
                    switchRightData()
                }
            }
        })
        }
}
```
viewModel中主要是为了作为数据的中转，从response中获取数据，然后View从viewModel中通过监听拿到数据
```kotlin
class StateLiveData<T> : MutableLiveData<BaseResult<T>>() {
}

class NavTabVM(application: Application, val navRepo: NavRepo) : BaseViewModel(application) {
     //StateLiveData是为了方便在MutableLiveData上多包装了个BaseResult
     val mWeChatList = StateLiveData<List<PublicBean>>()
     fun getWeChatList() {
        //使用协程，通过response请求数据
        viewModelScope.launch {
                navRepo.weChatList(mWeChatList)
            }
        }
}
```
response可以从网络中获取数据，也可以从本地中获取数据，都是在这里面处理就行，处理后的数据传给StateLiveData,
executeRequest是一个为了更加方便请求分装的
```kotlin
open class BaseRepository {
    suspend fun <T : Any> executeRequest(
            block: suspend () -> BaseResult<T>,
            stateLiveData: StateLiveData<T>
        ) {
            var baseResp = BaseResult<T>()
            try {
                baseResp.dataStatus = DataStatus.STATE_LOADING
                stateLiveData.postValue(baseResp)
                //将结果复制给baseResp
                baseResp = block.invoke()
                if (baseResp.errorCode == 0) {
                    baseResp.dataStatus = DataStatus.STATE_SUCCESS
                } else {
                    //服务器请求错误
                    baseResp.dataStatus = DataStatus.STATE_ERROR
                    baseResp.exception = ResultException(
                        baseResp.errorCode.toString(),
                        baseResp.errorMsg ?: ""
                    )
                }
            } catch (e: Exception) {
                //非后台返回错误，捕获到的异常
                baseResp.dataStatus = DataStatus.STATE_ERROR
                baseResp.exception = DealException.handlerException(e)
            } finally {
                stateLiveData.postValue(baseResp)
            }
        }
}
class NavRepo(val navApi: NavApi) : BaseRepository() {
    suspend fun weChatList(list: StateLiveData<List<PublicBean>>) {
        executeRequest({ navApi.weChatList() }, list)
    }
}
```

看到这一套MVVM的请求过程，是不是非常丝滑～

### 网络请求框架
协程很好用，项目中是配合retrofit和okhttp使用的，请求数据可以直接下面这样，关于协程的使用，可以推荐[万字长文 - Kotlin 协程进阶](https://juejin.cn/post/6950616789390721037)
```kotlin
interface NavApi {
    //获取公众号列表
    @GET("wxarticle/chapters/json")
    suspend fun weChatList(): BaseResult<List<PublicBean>>
}
```

### jetpack框架
jitpack中使用到的有lifecycle、viewModel、livedata、这几个是实现mvvm必须要会的。
项目中也用到了navigation、room、pagging 关于它们的使用，可以在项目中看下

### 后续升级计划
* base模块为了方便把一些和项目相关联的也放入了base模块中了，应该把业务相关的放到common模块中，和业务无关的放到base模块中
* 代码优化，里面有一些相同的代码块可以进行抽取
* 性能优化、屏幕适配等，把一些新技术都丢入其中













