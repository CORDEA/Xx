package jp.cordea.xx

enum class Source(val type: String) {
    OBSERVABLE("Observable"),
    SINGLE("Single"),
    MAYBE("Maybe"),
    COMPLETABLE("Completable"),
    FLOWABLE("Flowable");

    override fun toString(): String = this.type
}
