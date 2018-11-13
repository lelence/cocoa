var gulp = require('gulp'),
    mocha = require('gulp-mocha');

gulp.task('default', function() {
  return gulp.src(['test/*.js'], { read: false }) // 加载需要进行测试的所有文件
    .pipe(mocha()); // 用 mocha 模块进行测试
});
