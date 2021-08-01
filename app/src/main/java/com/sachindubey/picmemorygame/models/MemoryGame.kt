package com.sachindubey.picmemorygame.models

import com.sachindubey.picmemorygame.utils.DEFAULT_ICONS

class MemoryGame(private val boardSize: BoardSize)
{


    val cards:List<MemoryCard>
    var numpairs=0
   private var numcardsflip=0

    private var indexOfSingleSelectedCard: Int? = null

    init{

        val images= DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        val randomimages=(images+images).shuffled()
        cards=randomimages.map{MemoryCard(it)}

    }
    fun flipcard(position: Int):Boolean {
        numcardsflip++
        val card=cards[position]
        var foundMatch=false
        if (indexOfSingleSelectedCard == null) {
            // 0 or 2 selected cards previously
            restoreCards()
            indexOfSingleSelectedCard = position
        }
        else
        {
          foundMatch = checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null


        }

        card.isFaceUp=!card.isFaceUp
        return  foundMatch
    }

    private fun checkForMatch(position1: Int, position2: Int): Boolean {
        if (cards[position1].identifier != cards[position2].identifier) {
            return false
        }
        cards[position1].isMatched = true
        cards[position2].isMatched = true
        numpairs++
        return true

    }

    private fun restoreCards() {
        for (card:MemoryCard in cards) {
            if (!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }

    fun isCardFaceUp(position: Int): Boolean {
        return cards[position].isFaceUp
    }

    fun haveWonGame(): Boolean {
        return numpairs== boardSize.getNumPairs()

    }

    fun getNumMoves(): Int {

return numcardsflip/2
    }


}