package jp.co.yumemi.android.code_check.view_model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.Gson
import io.mockk.*
import jp.co.yumemi.android.code_check.model.MyResponse
import jp.co.yumemi.android.code_check.model.Repository
import jp.co.yumemi.android.code_check.net.GithubAppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException

@ExperimentalCoroutinesApi
class RepositoriesViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        // Exception in thread "main" java.lang.IllegalStateException 対策で必要
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        // Exception in thread "main" java.lang.IllegalStateException 対策で必要
        Dispatchers.resetMain()
    }

    @Test
    fun fetchRepositoriesTest() {
        val gson = Gson()
        val mockRepository = mockk<GithubAppRepository>()
        val target = RepositoriesViewModel(mockRepository)
        val jsonString = """
            {
              "items": [
                {
                  "name": "abc",
                  "owner": {
                    "avatar_url": "https://google.com"
                  },
                  "language": "C",
                  "stargazers_count": 1,
                  "watchers_count": 1,
                  "forks_count": 1,
                  "open_issues_count": 1
                }
              ]
            }
        """

        val result = gson.fromJson<MyResponse>(jsonString.trimIndent(), MyResponse::class.java)

        // coEvery を使用することで suspend fun の　mockRepository.getRepositories()　の戻り値を明示的に返せる
        coEvery { mockRepository.getRepositories("abc") } returns result

        // ViewModel の repositories が変更されたことを確認する為 observer を mock する
        val mockObserver = spyk<Observer<List<Repository>>>()
        target.repositories.observeForever(mockObserver)

        // テスト対象のメソッドを呼び出す
        target.search("abc")

        // mockObserver が mockRepository.getRepositories() の結果で呼ばれたことを確認する
        verify(exactly = 1) {
            mockObserver.onChanged(result.items)
        }
    }

    @Test
    fun fetchRepositoriesExceptionTest() {
        val mockRepository = mockk<GithubAppRepository>()
        val target = RepositoriesViewModel(mockRepository)


        val httpException = HttpException(
            Response.error<Any>(
                403, ResponseBody.create(
                    MediaType.parse("application/json"), ""
                )
            )
        )

        val unknownHostException = UnknownHostException()

        every { mockRepository.getRepositories("HttpException") } throws httpException
        every { mockRepository.getRepositories("UnknownHostException") } throws unknownHostException

        // ViewModel の searchError が変更されたことを確認する為 observer を mock する
        val mockObserver = spyk<Observer<Throwable>>()
        target.searchError.observeForever(mockObserver)

        // テスト対象のメソッドを呼び出す
        target.search("HttpException")

        // mockObserver が mockRepository.getRepositories() の結果で呼ばれたことを確認する
        verify(exactly = 1) {
            mockObserver.onChanged(httpException)
        }

        target.search("UnknownHostException")
        verify(exactly = 1) {
            mockObserver.onChanged(unknownHostException)
        }
    }
}