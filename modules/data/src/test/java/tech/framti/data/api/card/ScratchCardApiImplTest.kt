package tech.framti.data.api.card

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import tech.framti.api.card.ScratchCardService
import tech.framti.api.model.ScratchCardDto
import tech.framti.data.mapper.todomain.ScratchCardMapper
import tech.framti.domain.exception.ApiException
import tech.framti.domain.model.ScratchCard

class ScratchCardApiImplTest {

    private val scratchCardService: ScratchCardService = mockk()

    private lateinit var sut: ScratchCardApiImpl

    @Before
    fun setUp() {
        mockkObject(ScratchCardMapper)
        sut = ScratchCardApiImpl(scratchCardService)
    }

    @After
    fun tearDown() {
        unmockkObject(ScratchCardMapper)
    }

    @Test
    fun `activate returns mapped ScratchCard on successful response`() = runTest {
        val dto = ScratchCardDto(android = "300000")
        val expected = ScratchCard(android = 300000)

        coEvery { scratchCardService.activate("test-code") } returns Response.success(dto)
        every { ScratchCardMapper.map(dto) } returns expected

        val result = sut.activate("test-code")

        assertEquals(expected, result)
    }

    @Test
    fun `activate passes correct code to service`() = runTest {
        val dto = ScratchCardDto(android = "123")
        val mapped = ScratchCard(android = 123)

        coEvery { scratchCardService.activate(any()) } returns Response.success(dto)
        every { ScratchCardMapper.map(any<ScratchCardDto>()) } returns mapped

        sut.activate("my-special-code")

        coVerify { scratchCardService.activate("my-special-code") }
    }

    @Test
    fun `activate maps dto using ScratchCardMapper`() = runTest {
        val dto = ScratchCardDto(android = "42")
        val mapped = ScratchCard(android = 42)

        coEvery { scratchCardService.activate(any()) } returns Response.success(dto)
        every { ScratchCardMapper.map(dto) } returns mapped

        sut.activate("code")

        io.mockk.verify { ScratchCardMapper.map(dto) }
    }

    @Test(expected = ApiException::class)
    fun `activate throws ApiException when response body is null`() = runTest {
        coEvery { scratchCardService.activate("test-code") } returns Response.success(null)

        sut.activate("test-code")
    }

    @Test(expected = ApiException::class)
    fun `activate throws ApiException on error response`() = runTest {
        coEvery {
            scratchCardService.activate("test-code")
        } returns Response.error(400, "bad request".toResponseBody())

        sut.activate("test-code")
    }

    @Test(expected = ApiException::class)
    fun `activate throws ApiException on server error response`() = runTest {
        coEvery {
            scratchCardService.activate("test-code")
        } returns Response.error(500, "internal server error".toResponseBody())

        sut.activate("test-code")
    }

    @Test(expected = HttpException::class)
    fun `activate throws HttpException when service throws HttpException`() = runTest {
        coEvery {
            scratchCardService.activate("test-code")
        } throws HttpException(Response.error<ScratchCardDto>(500, "".toResponseBody()))

        sut.activate("test-code")
    }

    @Test(expected = RuntimeException::class)
    fun `activate rethrows exception when service throws`() = runTest {
        coEvery {
            scratchCardService.activate("test-code")
        } throws RuntimeException("network failure")

        sut.activate("test-code")
    }
}

