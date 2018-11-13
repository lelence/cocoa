var gulp = require('gulp');
var concat = require('gulp-concat');
var uglify = require('gulp-uglify');
var cssmin = require('gulp-clean-css');
var rename = require('gulp-rename');
var del = require('del');
var webserver = require('gulp-webserver');
var mocha = require('gulp-mocha');

var paths = {

    jss: [
        "src/**/*.js",
        "node_modules/jquery/dist/jquery.js",
        "node_modules/bootstrap/dist/js/bootstrap.bundle.js",
        "node_modules/socket.io-client/dist/socket.io.js"
    ],

    css: [
        "node_modules/bootstrap/dist/css/bootstrap.css"
    ]

}

// var jss = ["node_modules/jquery/dist/jquery.js"]

gulp.task('clean', function () {
    return del(['dist/**/*'], function () {
    })
});

gulp.task('css', function () {

    return gulp.src(paths.css)
        .pipe(concat('bootstrap.css'))
        .pipe(cssmin())
        .pipe(rename({suffix: '.min'}))
        .pipe(gulp.dest('dist/'));

});

gulp.task('css_copy', function () {

    return gulp.src(['src/**/**.css']).pipe(gulp.dest('dist/'));

});

gulp.task('js', function () {

    return gulp.src(paths.jss)
        .pipe(concat('build.js'))
        .pipe(uglify())
        .pipe(rename({suffix: '.min'}))
        .pipe(gulp.dest('dist/'));

});

gulp.task('html', function () {

    return gulp.src('src/**/**.html')
        .pipe(gulp.dest('dist'))

});

gulp.task('watch', function () {
    return gulp.watch('src/**/*.html', ['html']);
});

gulp.task('webserver', ['clean', 'html', 'js', 'css', 'css_copy', 'watch'], function () {
    return gulp.src('dist')
        .pipe(webserver({
            port: 3000,
            livereload: true,
            open: true
        }));
});

gulp.task('default', ['webserver']);


gulp.task('test1', function () {
  return gulp.src(['test/*.js'], { read: false }) // 加载需要进行测试的所有文件
    .pipe(mocha()); // 用 mocha 模块进行测试
});


