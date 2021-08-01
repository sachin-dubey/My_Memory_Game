package com.sachindubey.picmemorygame

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView

import androidx.recyclerview.widget.RecyclerView
import com.sachindubey.picmemorygame.models.BoardSize
import com.sachindubey.picmemorygame.models.MemoryCard
import kotlin.math.min


class MemoryAdapter(
    private val context: Context,
    private val boardSize: BoardSize,
    private val cards: List<MemoryCard>,
    private  val cardclicklistener:CardClickListener
) : RecyclerView.Adapter<MemoryAdapter.ViewHolder>() {


    interface CardClickListener{  //

        fun oncardclicked(position:Int)
    }


    companion object {
        private const val TAG = "MemoryBoardAdapter"
        private const val MARGIN_SIZE = 10
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardwidth=parent.width/boardSize.getwidth()- 2 * MARGIN_SIZE
        val cardheight=parent.height/boardSize.getheight()- 2 * MARGIN_SIZE

        val len=min(cardheight,cardwidth)


        val view = LayoutInflater.from(context)
            .inflate(R.layout.memory_card, parent, false)
        val layoutParams = view.findViewById<CardView>(R.id.cardview).layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.width = len
        layoutParams.height = len
        layoutParams.setMargins(MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE, MARGIN_SIZE)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.bind(position)
    }

    override fun getItemCount()=boardSize.numcards
    inner class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {

private  val imageButton=itemView.findViewById<android.widget.ImageButton>(R.id.imageButton)
        fun bind(position: Int) {
            val memorycard=cards[position]

            imageButton.setImageResource(if(cards[position].isFaceUp)cards[position].identifier else
                   R.drawable.ic_launcher_background)
            imageButton.alpha = if (memorycard.isMatched) .4f else 1.0f
//            val colorStateList = if (memorycard.isMatched) ContextCompat.getColorStateList(context, R.color.color_gray) else null
    //        ViewCompat.setBackgroundTintList(imageButton, colorStateList)
            imageButton.setOnClickListener {
                Log.i(TAG,"Clicked on button $position")
                cardclicklistener.oncardclicked(position)
            }

        }

    }
}
