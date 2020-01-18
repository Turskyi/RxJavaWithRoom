package ua.turskyi.rxjavawithroom.view

import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYouListener
import kotlinx.android.synthetic.main.list_item_country.view.*
import ua.turskyi.rxjavawithroom.R
import ua.turskyi.rxjavawithroom.data.entity.Country

class CountriesAdapter constructor(
    private val onCountryClickListener: (country: Country) -> Unit
) :
    RecyclerView.Adapter<CountriesAdapter.ViewHolder>() {

    private val countries: MutableList<Country> = mutableListOf()
    private val _visibilityLoader = MutableLiveData<Int>()
    val visibilityLoader: MutableLiveData<Int>
        get() = _visibilityLoader

    fun setData(newCountryList: List<Country>) {
        _visibilityLoader.postValue(VISIBLE)
        val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback(){
            override fun getOldListSize(): Int = countries.size

            override fun getNewListSize(): Int = newCountryList.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return countries[oldItemPosition] == newCountryList[newItemPosition]
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return countries[oldItemPosition].name == newCountryList[newItemPosition].name &&
                        countries[oldItemPosition].flag == newCountryList[newItemPosition].flag
            }
        })
        this.countries.clear()
        this.countries.addAll(newCountryList)
        if (newCountryList.isNotEmpty() && countries.containsAll(newCountryList)){
            _visibilityLoader.postValue(GONE)
        }
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context).inflate(R.layout.list_item_country, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val country = countries[position]
        holder.itemView.tvCountry.text = country.name
        showPicturesInSVG(country, holder)
    }

    private fun showPicturesInSVG(
        country: Country,
        holder: ViewHolder
    ) {
        val uri: Uri = Uri.parse(country.flag)

        GlideToVectorYou
            .init()
            .with(holder.itemView.context)
            .withListener(object : GlideToVectorYouListener {
                override fun onLoadFailed() {
                    showPicturesInWebView(holder, country)
                }

                override fun onResourceReady() {
                    holder.itemView.ivFlag.visibility = VISIBLE
                    holder.itemView.wvFlag.visibility = GONE
                }
            })
            .setPlaceHolder(R.drawable.anim_loading, R.drawable.ic_broken_image)
            .load(uri, holder.itemView.ivFlag)
    }

    private fun showPicturesInWebView(
        holder: ViewHolder,
        country: Country
    ) {
        holder.itemView.ivFlag.visibility = GONE
        holder.itemView.wvFlag.webViewClient = WebViewClient()
        holder.itemView.wvFlag.visibility = VISIBLE
        holder.itemView.wvFlag.setBackgroundColor(Color.TRANSPARENT)
        holder.itemView.wvFlag.setInitialScale(8)
        holder.itemView.wvFlag.loadUrl(country.flag)
    }

    override fun getItemCount(): Int = countries.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                onCountryClickListener.invoke(countries[layoutPosition])
            }
        }
    }
}

