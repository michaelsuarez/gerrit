load("@rules_java//java:defs.bzl", "java_library")

java_library(
    name = "truth",
    testonly = True,
    data = ["//lib:LICENSE-DO_NOT_DISTRIBUTE"],
    visibility = ["//visibility:public"],
    exports = ["@truth//jar"],
    runtime_deps = [
        ":diffutils",
        "//lib:guava",
        "//lib:junit",
    ],
)

java_library(
    name = "truth-java8-extension",
    testonly = True,
    data = ["//lib:LICENSE-DO_NOT_DISTRIBUTE"],
    visibility = ["//visibility:public"],
    exports = ["@truth-java8-extension//jar"],
    runtime_deps = [
        ":truth",
        "//lib:guava",
    ],
)

java_library(
    name = "truth-liteproto-extension",
    testonly = True,
    data = ["//lib:LICENSE-DO_NOT_DISTRIBUTE"],
    visibility = ["//visibility:private"],
    exports = ["@truth-liteproto-extension//jar"],
    runtime_deps = [
        ":truth",
        "//lib:guava",
        "//lib:protobuf",
    ],
)

java_library(
    name = "diffutils",
    testonly = True,
    data = ["//lib:LICENSE-DO_NOT_DISTRIBUTE"],
    visibility = ["//visibility:private"],
    exports = ["@diffutils//jar"],
)

java_library(
    name = "truth-proto-extension",
    testonly = True,
    data = ["//lib:LICENSE-DO_NOT_DISTRIBUTE"],
    visibility = ["//visibility:public"],
    exports = [
        ":truth-liteproto-extension",
        "@truth-proto-extension//jar",
    ],
    runtime_deps = [
        ":truth",
        ":truth-liteproto-extension",
        "//lib:guava",
        "//lib:protobuf",
    ],
)
