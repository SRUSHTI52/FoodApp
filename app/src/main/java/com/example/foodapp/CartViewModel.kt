import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodapp.CartDatabase
import com.example.foodapp.CartItem
import com.example.foodapp.CartItemDao
import kotlinx.coroutines.launch


class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val cartItemDao: CartItemDao = CartDatabase.getDatabase(application).cartItemDao()

    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> get() = _cartItems

    private var userId: Int = -1

    fun setUserId(userId: Int) {
        this.userId = userId
        if (userId != -1) {
            refreshCartItems()
        }
    }

//    private fun refreshCartItems() {
//        userId.let { id ->
//            viewModelScope.launch {
//                val items = cartItemDao.getCartItems(id).value ?: emptyList()
//                _cartItems.postValue(items)
//            }
//        }
//    }

    private fun refreshCartItems() {
        userId?.let { id ->
            viewModelScope.launch {
                cartItemDao.getCartItems(id).observeForever { items ->
                    _cartItems.postValue(items)
                }
            }
        }
    }


    fun addCartItem(cartItem: CartItem) {
        viewModelScope.launch {
            cartItemDao.insertCartItem(cartItem)
            // Optionally refresh the cart items
            refreshCartItems()
        }
    }

    suspend fun getCartItemCount(userId: Int): Int {
        return cartItemDao.getCartItemCount(userId)
    }


//    fun addCartItem(cartItem: CartItem) {
//        viewModelScope.launch {
//            // Insert the cart item
//            cartItemDao.insertCartItem(cartItem)
//
//            // Get the count of cart items for the user
//            val count = cartItemDao.getCartItemCount(cartItem.userId)
//
//            // Show a toast with the count
//            Toast.makeText(getApplication(), "Items in cart: $count", Toast.LENGTH_SHORT).show()
//
//            // Refresh cart items after adding a new item
//            refreshCartItems()
//        }
//    }


    fun updateCartItem(cartItem: CartItem) {
        viewModelScope.launch {
            cartItemDao.updateCartItem(cartItem)
            // Refresh cart items after updating an item
            refreshCartItems()
        }
    }

    fun deleteCartItem(cartItem: CartItem) {
        viewModelScope.launch {
            cartItemDao.deleteCartItem(cartItem)
            refreshCartItems()
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartItemDao.deleteAllCartItems(userId)
            refreshCartItems()
        }
    }
}




//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.viewModelScope
//import com.example.foodapp.CartDatabase
//import com.example.foodapp.CartItem
//import com.example.foodapp.CartItemDao
//import com.example.foodapp.ItemDao
//import kotlinx.coroutines.launch
//
//class CartViewModel(application: Application) : AndroidViewModel(application) {
//    private val cartItemDao: CartItemDao = CartDatabase.getDatabase(application).cartItemDao()
//    private val itemDao: ItemDao = CartDatabase.getDatabase(application).itemDao()
//
//    private val _cartItems = MutableLiveData<List<CartItem>>()
//    val cartItems: LiveData<List<CartItem>> get() = _cartItems
//
//    private var userId: Int = 0
//
//    init {
//        // Observe the LiveData from the DAO and update _cartItems
//        // Ensure userId is set before this operation
//        if (userId != 0) {
//            cartItemDao.getCartItems(userId).observeForever { items ->
//                _cartItems.postValue(items)
//            }
//        }
//    }
//
//    fun setUserId(userId: Int) {
//        this.userId = userId
//        // Update the LiveData when userId changes
//        if (userId != 0) {
//            viewModelScope.launch {
//                cartItemDao.getCartItems(userId).observeForever { items ->
//                    _cartItems.postValue(items)
//                }
//            }
//        }
//    }
//
//    fun addCartItem(cartItem: CartItem) {
//        viewModelScope.launch {
//            cartItemDao.insertCartItem(cartItem)
//            // No need to manually update _cartItems, LiveData will update automatically
//        }
//    }
//
//    fun updateCartItem(cartItem: CartItem) {
//        viewModelScope.launch {
//            cartItemDao.updateCartItem(cartItem)
//            // No need to manually update _cartItems, LiveData will update automatically
//        }
//    }
//
//    fun deleteCartItem(cartItem: CartItem) {
//        viewModelScope.launch {
//            cartItemDao.deleteCartItem(cartItem)
//            // No need to manually update _cartItems, LiveData will update automatically
//        }
//    }
//
//    fun clearCart() {
//        viewModelScope.launch {
//            cartItemDao.deleteAllCartItems(userId)
//            // No need to manually update _cartItems, LiveData will update automatically
//        }
//    }
//}

//import android.app.Application
//import androidx.lifecycle.AndroidViewModel
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.viewModelScope
//import com.example.foodapp.CartDatabase
//import com.example.foodapp.CartItem
//import com.example.foodapp.CartItemDao
//import com.example.foodapp.DataRepository
//import com.example.foodapp.ItemDao
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//class CartViewModel(application: Application) : AndroidViewModel(application) {
//    private val cartItemDao: CartItemDao = CartDatabase.getDatabase(application).cartItemDao()
//    private val itemDao: ItemDao = CartDatabase.getDatabase(application).itemDao()
//
//    private val _cartItems = MutableLiveData<List<CartItem>>()
//    val cartItems: LiveData<List<CartItem>> get() = _cartItems
//
//    init {
//        // Observe the LiveData from the DAO and update _cartItems
//        cartItemDao.getCartItems().observeForever { items ->
//            _cartItems.postValue(items)
//        }
//    }
//
//    fun addCartItem(cartItem: CartItem) {
//        viewModelScope.launch {
//            cartItemDao.insertCartItem(cartItem)
//            // No need to manually update _cartItems, LiveData will update automatically
//        }
//    }
//
//    fun updateCartItem(cartItem: CartItem) {
//        viewModelScope.launch {
//            cartItemDao.updateCartItem(cartItem)
//            // No need to manually update _cartItems, LiveData will update automatically
//        }
//    }
//
//    fun deleteCartItem(cartItem: CartItem) {
//        viewModelScope.launch {
//            cartItemDao.deleteCartItem(cartItem)
//            // No need to manually update _cartItems, LiveData will update automatically
//        }
//    }
//
//    fun clearCart() {
//        viewModelScope.launch {
//            cartItemDao.deleteAllCartItems()
//            // No need to manually update _cartItems, LiveData will update automatically
//        }
//    }
//
////    private fun deleteFirstFourItems() {
////        CoroutineScope(Dispatchers.IO).launch {
////            val firstFourItems = itemDao.getFirstFourItems()
////            if (firstFourItems.isNotEmpty()) {
////                itemDao.deleteItems(firstFourItems)
////            }
////        }
////    }
//
//}

