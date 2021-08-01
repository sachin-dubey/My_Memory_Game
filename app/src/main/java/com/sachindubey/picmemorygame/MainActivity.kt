package com.sachindubey.picmemorygame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sachindubey.picmemorygame.models.BoardSize
import com.sachindubey.picmemorygame.models.MemoryGame


class MainActivity : AppCompatActivity() {
    companion object { // like static in java.
        private const val TAG = "MainActivity"
    }
    private lateinit var clRoot: ConstraintLayout
    private lateinit var adapter: MemoryAdapter
    private lateinit var memorygame: MemoryGame
    private lateinit var gameboard:RecyclerView
    private lateinit var nomovess:TextView
    private lateinit var nopairss:TextView
    private var boardSize:BoardSize=BoardSize.EASY
   


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clRoot = findViewById(R.id.clRoot)
        gameboard=findViewById(R.id.gameboard)
        nomovess=findViewById(R.id.nomoves)
        nopairss=findViewById(R.id.nopairs)


      setupboard()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
      menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       when(item.itemId){
           R.id.mi_refresh->{
                ///
               if(memorygame.getNumMoves()>0 && !memorygame.haveWonGame()){
                   showAlertDialog("Quit your current game?",null,View.OnClickListener {
 setupboard()
                   }
                   )
               }
               else {
                   setupboard()
               }
       }
           R.id.mi_new_size->{
               showNewSizeDialog()

           }
       }
        return  super.onOptionsItemSelected(item)
    }

    private fun showNewSizeDialog() {

        val boardSizeview=LayoutInflater.from(this,).inflate(R.layout.dialog_board_size,null)
        val radiogroupsize=boardSizeview.findViewById<RadioGroup>(R.id.radiogroup)
        when (boardSize) {
            BoardSize.EASY -> radiogroupsize.check(R.id.rbeasy)
            BoardSize.MEDIUM -> radiogroupsize.check(R.id.rbmedium)
            BoardSize.HARD -> radiogroupsize.check(R.id.rbhard)
        }
        showAlertDialog("Choose new size",boardSizeview,View.OnClickListener {

            boardSize = when (radiogroupsize.checkedRadioButtonId) {
                R.id.rbeasy -> BoardSize.EASY
                R.id.rbeasy -> BoardSize.MEDIUM
                else -> BoardSize.HARD
            }
            setupboard()
            })
    }

    private fun showAlertDialog(title:String,view: View?,positiveClickListener: View.OnClickListener) {
      AlertDialog.Builder(this).setTitle(title).setView(view).setNegativeButton("Cancel",null).setPositiveButton("OK")
      {
          _,_->
          positiveClickListener.onClick(null)

      }.show()
    }

    private fun setupboard() {

        when (boardSize){
            BoardSize.EASY -> {
                nomovess.text = "Easy: 4 x 2"
                nopairss.text = "Pairs: 0/4"
            }
            BoardSize.MEDIUM -> {
               nomovess.text = "Medium: 6 x 3"
               nopairss.text = "Pairs: 0/9"
            }
            BoardSize.HARD -> {
               nomovess.text= "Hard: 6 x 4"
                 nomovess.text= "Pairs: 0/12"
            }
        }
        memorygame=MemoryGame(boardSize)


        adapter=MemoryAdapter(this,boardSize,memorygame.cards,object:MemoryAdapter.CardClickListener{
            override fun oncardclicked(position: Int) {

                updateGameWithFlip(position)
            }

        })
        gameboard.adapter=adapter
        gameboard.setHasFixedSize(true)
        gameboard.layoutManager=GridLayoutManager(this,boardSize.getwidth())
    }


    private fun updateGameWithFlip(position: Int){

        if(memorygame.haveWonGame())
        {
            Snackbar.make(clRoot, "You already won!", Snackbar.LENGTH_LONG).show()
            return
        }
        if (memorygame.isCardFaceUp(position)) {
            Snackbar.make(clRoot, "Invalid move!", Snackbar.LENGTH_SHORT).show()
            return
        }
        if(memorygame.flipcard(position))
        {
            nopairss.text="Pairs:${memorygame.numpairs}/ ${boardSize.getNumPairs()}"
            if (memorygame.haveWonGame()) {
                Snackbar.make(clRoot, "You won! Congratulations.", Snackbar.LENGTH_LONG).show()
            }
        }
        nomovess.text= "Moves: ${memorygame.getNumMoves()}"
        adapter.notifyDataSetChanged()




    }
}