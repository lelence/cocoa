var gulp = require('gulp'),
    concat = require('gulp-concat'),
    uglify = require('gulp-uglify'),
    rename = require('gulp-rename'),
    del = require('del'),
    webserver = require('gulp-webserver');

var paths = {

    jss: [
        "src/**/*.js",
        "node_modules/jquery/dist/jquery.js",
        "node_modules/socket.io-client/dist/socket.io.js"
    ]

}

// var jss = ["node_modules/jquery/dist/jquery.js"]

gulp.task('clean', function () {
    return del(['dist/**/*'], function () {
    })
});

gulp.task('js', function () {

    return gulp.src(paths.jss)
        .pipe(concat('build.js'))
        .pipe(uglify())
        .pipe(rename({suffix: '.min'}))
        .pipe(gulp.dest('dist/'));

});

gulp.task('html', function () {

    return gulp.src('src/**/*.html')
        .pipe(gulp.dest('dist'))

});

gulp.task('watch', function () {
    return gulp.watch('src/**/*.html', ['html']);
});

gulp.task('webserver', ['clean', 'html', 'js', 'watch'], function () {
    return gulp.src('dist')
        .pipe(webserver({
            port: 3000,
            livereload: true,
            open: true
        }));
});


gulp.task('default', ['webserver']);