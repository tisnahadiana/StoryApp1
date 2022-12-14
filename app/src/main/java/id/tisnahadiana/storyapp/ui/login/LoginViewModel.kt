package id.tisnahadiana.storyapp.ui.login

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.*
import id.tisnahadiana.storyapp.data.local.datastore.LoginPreferences
import id.tisnahadiana.storyapp.data.repository.UserRepository
import id.tisnahadiana.storyapp.di.Injection

class LoginViewModel(
    private val userRepository: UserRepository,
    private val loginPreferences: LoginPreferences
) : ViewModel() {
    fun login(email: String, password: String) = userRepository.login(email, password)

    fun register(name: String, email: String, password: String) =
        userRepository.register(name, email, password)

    fun saveToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            loginPreferences.saveToken(token)
        }
    }

    fun checkIfFirstTime(): LiveData<Boolean> {
        return loginPreferences.isFirstTime().asLiveData()
    }

    class LoginViewModelFactory private constructor(
        private val userRepository: UserRepository,
        private val loginPreferences: LoginPreferences
    ) :
        ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(userRepository, loginPreferences) as T
            }

            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }

        companion object {
            @Volatile
            private var instance: LoginViewModelFactory? = null
            fun getInstance(
                loginPreferences: LoginPreferences
            ): LoginViewModelFactory =
                instance ?: synchronized(this) {
                    instance ?: LoginViewModelFactory(
                        Injection.provideUserRepository(),
                        loginPreferences
                    )
                }
        }
    }
}