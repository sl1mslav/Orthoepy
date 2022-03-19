package com.example.orthoepy.wordsmodel

typealias WordsStoreListener = (words: List<Word>) -> Unit

class WordsStoreService {

    private var wordsStore = mutableListOf<Word>()

    private val listeners = mutableSetOf<WordsStoreListener>()

    fun getWords(): List<Word> = wordsStore.filter { it.isBought == "false" }

    fun buyWord(word: Word) {
        val indexToBuy = wordsStore.indexOfFirst { it.id == word.id }
        if (indexToBuy != -1) {
            wordsStore[indexToBuy].isBought = "true"
            wordsStore.removeAt(indexToBuy)
            notifyChanges()
        }
    }

    fun markFavourite(word: Word) {
        //TODO: Move this to Dictionary
        val indexToMark = wordsStore.indexOfFirst { it.id + 1 == word.id }
        if (indexToMark != -1) {
            when (wordsStore[indexToMark].isFavourite) {
                "true" -> wordsStore[indexToMark].isFavourite = "false"
                "false" -> wordsStore[indexToMark].isFavourite = "true"
            }
            notifyChanges()
        }
    }

    fun memorize(word: Word) {
        //TODO: Move this to Dictionary
        if (word.memorizationLevel < 5) {
            word.memorizationLevel++
            notifyChanges()
        }
        else return
    }

    fun findUser(word: Word, textToMatch: String) {
        //TODO: Implement this method to show matching words in RecyclerView
    }

    fun addListener(listener: WordsStoreListener) {
        listeners.add(listener)
        listener.invoke(wordsStore)
    }

    fun removeListener(listener: WordsStoreListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach { it.invoke(wordsStore) }
    }

    init {
        wordsStore = mutableListOf(
            Word(1, "аэропорты", "аэропо́рты", 5, 0, "false", "false"),
            Word(2, "банты", "ба́нты", 1, 0, "false", "false"),
            Word(3, "бороду", "бо́роду", 1, 0, "false", "false"),
            Word(4, "бухгалтеров", "бухга́лтеров", 4, 0, "false", "false"),
            Word(5, "вероисповедание", "вероиспове́дание", 9, 0, "false", "false"),
            Word(6, "гражданство", "гражда́нство", 5, 0, "false", "false"),
            Word(7, "дефис", "дефи́с", 3, 0, "false", "false"),
            Word(8, "дешевизна", "дешеви́зна", 5, 0, "false", "false"),
            Word(9, "диспансер", "диспансе́р", 7, 0, "false", "false"),
            Word(10, "договоренность", "договорённость", 7, 0, "false", "false"),
            Word(11, "документ", "докуме́нт", 5, 0, "false", "false"),
            Word(12, "досуг", "досу́г", 3, 0, "false", "false"),
            Word(13, "еретик", "ерети́к", 4, 0, "false", "false"),
            Word(14, "жалюзи", "жалюзи́", 0, 0, "false", "false"),
            Word(15, "значимость", "зна́чимость", 0, 0, "false", "false"),
            Word(16, "иксы", "и́ксы", 0, 0, "false", "false"),
            Word(17, "каталог", "катало́г", 0, 0, "false", "false"),
            Word(18, "квартал", "кварта́л", 0, 0, "false", "false"),
            Word(19, "километр", "киломе́тр", 0, 0, "false", "false"),
            Word(20, "конусы", "ко́нусы", 0, 0, "false", "false"),
            Word(21, "корысть", "коры́сть", 0, 0, "false", "false"),
            Word(22, "краны", "кра́ны", 0, 0, "false", "false"),
            Word(23, "лекторы", "ле́кторы", 0, 0, "false", "false"),
            Word(24, "лыжня", "лыжня́", 0, 0, "false", "false"),
            Word(25, "местностей", "ме́стностей", 0, 0, "false", "false")
        )
    }
}