package com.example.kkon

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_board.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import java.io.IOException

// ?
class Board : AppCompatActivity() {
    var list:ArrayList<ItemObject> = ArrayList()
    var boardData:ArrayList<BoardData> = ArrayList() // 공지사항
    var boardData2:ArrayList<BoardData> = ArrayList() // 취업공지
    var boardData3:ArrayList<BoardData> = ArrayList() // 공지사항
    var boardData4:ArrayList<BoardData> = ArrayList() // 특강공지

    val addr1 = "http://home.konkuk.ac.kr/cms/Site/ControlReader/MiniBoardList/miniboard_list_etype_ku_board.jsp?siteId=im&menuId=11892&menuId=11896&forumId=11914&forumId=18240&titleImg=/cms/Site/UserFiles/Image/internet/main_notice_title.gif&tabImg=/cms/Site/UserFiles/Image/internet/main_notice_tab0&rowsNum=6&moreImg=/cms/Site/UserFiles/Image/internet/btn_more.gif"
    val addr2 = "http://home.konkuk.ac.kr/cms/Site/ControlReader/MiniBoardList/miniboard_list_etype_ku_board.jsp?siteId=im&menuId=3266851&menuId=12351727&forumId=12368452&forumId=12368521&titleImg=/cms/Site/UserFiles/Image/internet/main_board_title.gif&tabImg=/cms/Site/UserFiles/Image/internet/main_board_tab0&rowsNum=6&moreImg=/cms/Site/UserFiles/Image/internet/btn_more.gif"

    val linkStr = "http://home.konkuk.ac.kr:80/cms/Common/MessageBoard/ArticleRead.do?forum=11914&id=" // 뒤에 아이디값 추가하면 접속됨
    val linkStr2 = "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum="



    lateinit var adapter1Board: BoardDataAdapter
    lateinit var adapter2Board: BoardDataAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        init()
        Log.i("on", "onCreate()")
        //adapterClick()
    }

    fun init() {
        // 데이터 web에서 읽어와서 내부 자료구조(ArrayList)에 저장
        doParse().execute()
        doParse2().execute()


        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerview.layoutManager = layoutManager

        val layoutManager2 = LinearLayoutManager(applicationContext)
        recyclerview2.layoutManager = layoutManager2

        adapter1Board = BoardDataAdapter(boardData)
        recyclerview.adapter = adapter1Board

        adapter2Board = BoardDataAdapter(boardData2)
        recyclerview2.adapter = adapter2Board

        spinner_board.onItemSelectedListener = SpinnerSelectedListener()

    }



    // adapter listener
    fun adapterClick() {
        adapter1Board.itemClickListener = object: BoardDataAdapter.OnItemClickListener {
            override fun OnItemClick(holder: BoardDataAdapter.ViewHolder, view: View, data: BoardData, position: Int) {
//                Toast.makeText(applicationContext, data.dId, Toast.LENGTH_SHORT).show()
//                Log.v("test1", data.dId)

                val i = Intent(applicationContext, Board_detail::class.java)
                i.putExtra("menuId", data.dId)
                startActivity(i)

            }
        }
        recyclerview.adapter = adapter1Board

        adapter2Board.itemClickListener = object: BoardDataAdapter.OnItemClickListener {
            override fun OnItemClick(holder: BoardDataAdapter.ViewHolder, view: View, data: BoardData, position: Int) {
//                Toast.makeText(applicationContext, data.dId, Toast.LENGTH_SHORT).show()
//                Log.v("test2", data.dId)

                val i = Intent(applicationContext, Board_detail::class.java)
                i.putExtra("menuId", data.dId)
                startActivity(i)
            }
        }
        recyclerview2.adapter = adapter2Board
    }


    // spinner 선택되어있는데 안뜨는 문제 해결: 네트워크 문제임
    inner class SpinnerSelectedListener : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {

        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            Toast.makeText(parent?.context, parent?.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show()

            // spinner_board.selectedItem.toString()
            if (parent?.getItemAtPosition(position).toString() == "KU 행정실 공지사항") {
                adapter1Board = BoardDataAdapter(boardData)
                recyclerview.adapter = adapter1Board
                adapter1Board.notifyDataSetChanged()

                adapter2Board = BoardDataAdapter(boardData3)
                recyclerview2.adapter = adapter2Board
                adapter2Board.notifyDataSetChanged()
                txt_recyclerView2.text = "취업공지"
                txt_recyclerView1.setBackgroundResource(R.drawable.bg_txt1)
                txt_recyclerView2.setBackgroundResource(R.drawable.bg_txt1)

                adapterClick()
            } else {
                adapter1Board = BoardDataAdapter(boardData2)
                recyclerview.adapter = adapter1Board
                adapter1Board.notifyDataSetChanged()

                adapter2Board = BoardDataAdapter(boardData4)
                recyclerview2.adapter = adapter2Board
                adapter2Board.notifyDataSetChanged()
                txt_recyclerView2.text = "특강공지"
                txt_recyclerView1.setBackgroundResource(R.drawable.bg_txt3)
                txt_recyclerView2.setBackgroundResource(R.drawable.bg_txt3)

                adapterClick()
            }


        }

    }


    inner class doParse : AsyncTask<Void, Void, Void>() {

        override fun doInBackground(vararg params: Void): Void? {
            try {
                // KU 행정실 공지사항
                val doc1 = Jsoup.connect(addr1).get()
                // Seoul Accord
                val doc2 = Jsoup.connect(addr2).get()

                val entries1 = doc1.select("div#board1").select("dl")
                val entries2 = doc2.select("div#board1").select("dl")
//                val eSize = entries1.size // 공지사항 개수
//                Log.v("table", eSize.toString()) // 6개로 잘 받아짐

                // 공지사항
                for (e in entries1) {
                    val title = e.select("dt").text()
                    val date = e.select("dd").text()
                    val hrefStr = e.select("dt a").attr("href")
                    val hrefSize = hrefStr.length
                    val menuId = hrefStr.substring(hrefSize-10 .. hrefSize-3) // 각 페이지 링크 접속하게 해주는 id 추출
//                    Log.v("entry", title)
//                    Log.v("entry", date)
//                    Log.v("menuId", menuId)

                    boardData.add(BoardData(menuId, title, date))
                }

                // 취업공지
                for (e in entries2) {
                    val title = e.select("dt").text()
                    val date = e.select("dd").text()
                    val hrefStr = e.select("dt a").attr("href")
                    val hrefSize = hrefStr.length
                    val menuId = hrefStr.substring(hrefSize-10 .. hrefSize-3)

                    boardData2.add(BoardData(menuId, title, date))
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(result: Void?) {
        }
    }


    inner class doParse2 : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void): Void? {
            try {
                // KU 행정실 공지사항
                val doc1 = Jsoup.connect(addr1).get()
                // Seoul Accord
                val doc2 = Jsoup.connect(addr2).get()

                val entries3 = doc1.select("div#board2").select("dl")
                val entries4 = doc2.select("div#board2").select("dl")


                for (e in entries3) {
                    val title = e.select("dt").text()
                    val date = e.select("dd").text()
                    val hrefStr = e.select("dt a").attr("href")
                    val hrefSize = hrefStr.length
                    val menuId = hrefStr.substring(hrefSize-10 .. hrefSize-3)

                    boardData3.add(BoardData(menuId, title, date))
                }

                for (e in entries4) {
                    val title = e.select("dt").text()
                    val date = e.select("dd").text()
                    val hrefStr = e.select("dt a").attr("href")
                    val hrefSize = hrefStr.length
                    val menuId = hrefStr.substring(hrefSize-9 .. hrefSize-3) // 이것만 7개

                    boardData4.add(BoardData(menuId, title, date))
                }



            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(result: Void?) {

        }

    }

}