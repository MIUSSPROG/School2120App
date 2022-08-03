package com.example.school2120app.presentation.contacts

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.school2120app.databinding.FragmentContactDetailBinding
import com.example.school2120app.domain.model.contacts.ContactInfo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ContactsDetailFragment: BottomSheetDialogFragment() {

    private lateinit var binding: FragmentContactDetailBinding
    private val args: ContactsDetailFragmentArgs by navArgs()
    private var contactItemInfo: ContactInfo? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentContactDetailBinding.inflate(inflater, container, false)
        contactItemInfo = args.contactItemInfo

        binding.apply {
            tvAddressContactDetail.text = contactItemInfo?.address
            tvEmailContactDetail.text = contactItemInfo?.email
            tvNameContactDetail.text = contactItemInfo?.name
            tvPhoneContactDetail.text = contactItemInfo?.phone
            tvPositionContactDetail.text = contactItemInfo?.position
            Glide.with(root.context)
                .load(contactItemInfo?.photoUrl)
                .circleCrop()
                .into(imgvAvatarContactDetail)

            btnMailContactDetail.setOnClickListener {
                contactItemInfo?.let {
                    val emailIntent = Intent(Intent.ACTION_SENDTO)
                    emailIntent.data = Uri.parse("mailto:" + it.email)
                    startActivity(emailIntent)
                }
            }

            btnNavContactDetail.setOnClickListener {
                contactItemInfo?.let {
                    parentFragmentManager.setFragmentResult(REQUEST_CODE, bundleOf(EXTRA_CONTACT_SELECTED to it))
                    dismiss()
                }
            }

            btnPhoneContactDetail.setOnClickListener {
                contactItemInfo?.let {
                    val phoneIntent = Intent(Intent.ACTION_DIAL)
                    phoneIntent.data = Uri.parse("tel:" + it.phone)
                    startActivity(phoneIntent)
                }
            }
        }

        return binding.root

    }

    companion object{
        const val REQUEST_CODE = "CONTACTS_DETAIL"
        const val EXTRA_CONTACT_SELECTED = "EXTRA_CONTACT_SELECTED"
    }
}