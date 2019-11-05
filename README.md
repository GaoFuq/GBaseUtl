# GBaseUtl
我总结的常用android组件或者工具。直接用。
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.GaoFuq:GBaseUtl:1.0.3'
	}


#使用

```
  @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPermission();
        NavController controller = NavHostFragment.findNavController(this);
        //地址选择器
        binding.btnAddress.setOnClickListener(v->{
            if(addressPicker==null){
                addressPicker = new AddressPicker(getActivity());
                addressPicker.setAddressListener((province, city, district) -> Toast.makeText(getActivity(), province+city+district, Toast.LENGTH_SHORT).show());
            }
            addressPicker.show();
        });

	//横向均分item
        binding.recycleView.setOnClickListener(v -> controller.navigate(R.id.action_home_to_RVBindingRecycleView));

        binding.btnSelect.setOnClickListener(v -> controller.navigate(R.id.action_home_to_dropSelect));

	//底部圆角日历Dialog
        binding.btnBottomCalendar.setOnClickListener(v -> {
            if(calenderDialog==null) {
                calenderDialog = new BottomCalenderDialog(getActivity());
                calenderDialog.setOnCalenderSelectListener((year, month, day) -> {
                    Toast.makeText(getActivity(), year + "年" + month + "月" + day + "日", Toast.LENGTH_SHORT).show();
                });
            }
            calenderDialog.show();
        });

        List<String> list = new ArrayList<>();
        for (int i = 0; i <5 ; i++) {
            list.add("test"+i);
        }
        binding.btnBottomDialog1.setOnClickListener(v -> {
            if(dialog==null) {
                dialog = new BottomChooseDialog<String>(getActivity()) {
                    @Override
                    protected String getTitle() {
                        return "标题";
                    }

                    @Override
                    public List<String> getDataList() {
                        return list;
                    }

                    @Override
                    protected void onConfirmClicked(String content) {
                        Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
                    }
                };
            }
            dialog.show();
        });
	
	//圆角Dialog
        binding.btnRoundDialog.setOnClickListener(v -> {
            new RoundDialog(getActivity()).setTitle("标题").setContent("内容").show();
        });

	//截屏
        binding.btnScreenshot.setOnClickListener(v -> {
                ScreenShotUtil.shot(getActivity(), "ABCDEFG", System.currentTimeMillis() + "_haha");
        });
	
	
	//改变状态栏
        binding.btnStatusBar.setOnClickListener(v -> {
            StatusBarUtils.setStatusBarColor(getActivity(), Color.RED);
        });
	

	//IO操作
        binding.btnWriteText2File.setOnClickListener(v -> {
                FileUtil.writeTxtToFile("fdwafnuhfhjpfafj648646", Environment.getExternalStorageDirectory().getPath()+"/ABCDEFG/", "abc.txt");
            Toast.makeText(getActivity(),"/ABCDEFG/abc.txt" , Toast.LENGTH_LONG).show();

        });

    }



       private void initPermission() {
              String[] permissions = {Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
              Permission.requestPermissions(getActivity(), 101, permissions, new Permission.OnPermissionListener() {
                  @Override
                  public void onPermissionGranted() {
                  }

                  @Override
                  public void onPermissionDenied() {
                      Permission.showTipsDialog(getActivity());
                  }
              });

          }
}

```
