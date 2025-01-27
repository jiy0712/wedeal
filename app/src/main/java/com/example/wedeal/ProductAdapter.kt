package com.example.wedeal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//상품 데이터를 리사이클뷰를 이용해 표시하기 위한
class ProductAdapter(private val productList: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    //상품 인터페이스
    interface OnItemClickListener {
        fun onItemClick(product: Product) //아이템 클릭시 호출 (메서드)
    }

    private var listener: OnItemClickListener? = null //클릭 저장 변수

    //클릭 설명 메서드
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    //뷰홀더
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    //뷰홀더와 데이터 연결
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    //상품 갯수
    override fun getItemCount(): Int {
        return productList.size
    }

    //뷰홀더 클래스 (리사이클뷰의 각각의 상품 관리)
    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productName: TextView = itemView.findViewById(R.id.productName)
        private val productPrice: TextView = itemView.findViewById(R.id.productPrice)

        //상품 데이터를 뷰바인딩
        fun bind(product: Product) {
            productName.text = product.name
            productPrice.text = product.price

            itemView.setOnClickListener {
                listener?.onItemClick(product)
            }
        }
    }
}

