package com.knowcup.async;

import com.knowcup.async.service.AsyncService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AsyncApplicationTests {

	@Autowired
	AsyncService asyncService;

	@Test
	public void contextLoads() {
	}

	/**
	 * 开启三个简单线程
	 * @throws InterruptedException
	 */
	@Test
	public void testAsync1() throws InterruptedException {
		System.out.println("=====" + Thread.currentThread().getName() + "=========");
		asyncService.asyncMethod1();
		asyncService.asyncMethod1();
		asyncService.asyncMethod1();
	}

	/**
	 * 开启三个带参数的线程
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Test
	public void testAsync2() throws InterruptedException, ExecutionException {
		System.out.println("=====" + Thread.currentThread().getName() + "=========");
		asyncService.asyncMethod2("Async 1");
		asyncService.asyncMethod2("Async 2");
		asyncService.asyncMethod2("Async 3");
	}

	/**
	 * 开启两个个带参数的线程  一个无返回结果的线程
	 * 等待返回结果
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Test
	public void testAsync3() throws InterruptedException, ExecutionException {
		System.out.println("=====" + Thread.currentThread().getName() + "=========");
		Future<String> task1 = asyncService.asyncMethod3("Async 1");
		asyncService.asyncMethod2("Async 2");
		Future<String> task3 = asyncService.asyncMethod3("Async 3");

		while(true) {
			if(task1.isDone() && task3.isDone()) {
				System.out.println("Task1 result: "+ task1.get());
				System.out.println("Task2 result: "+ task3.get());
				break;
			}
			Thread.sleep(1000);
		}

		asyncService.asyncMethod3("Async3 1");
	}
}
