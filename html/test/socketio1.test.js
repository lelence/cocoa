// var expect = require('chai').expect;

// describe('判断对象是否为数组的测试', function(){ // describe 测试的描述
//   it('[1,2,3] 应该是一个数组', function(){ // it 测试的核心
//     expect(1 == 1).to.be.ok; // 利用 expect 判断 isArray([1,2,3]) 的结果是否为真
//   });
// });

describe('socket.io client testing', function() {
	it('test1', function() {

		var i = 0;

		var connections = 20;

		var interval = setInterval(function() {

			var socket = require('socket.io-client')('http://localhost:9277', {
				forceNew: true
			});

			socket.on('connect', function() {

				console.log("hahah: " + i);

				socket.emit('/rpc',{'id': 99, 'method': 'getBalance', 'jsonrpc': '2.0', 'params': {'owner': 'Toan', 'delegateAddress': 'haha'}});

				i++;
			});

			socket.on('disconnect', function() {
				console.log("dudud")
			});

			socket.on('balance', function(data) {
				console.log(i + "#h: " + data)
			})

			if (i >= connections) {
				clearInterval(interval);
			}

		}, 100);

		// setInterval(function() {
		//  console.log(i);
		// }, 1000)


	});
});



// describe('判断对象是否为数组的测试', function() { // describe 测试的描述
//   it('[1,2,3] 应该是一个数组', function() { // it 测试的核心

//     socket.on('connect', function() {
//     	console.log("hahah")
//     });

//     // expect(1 == 1).to.be.ok; // 利用 expect 判断 isArray([1,2,3]) 的结果是否为真
//   });
// });