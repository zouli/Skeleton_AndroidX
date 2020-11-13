package com.riverside.skeleton.android.widget.commonselect;

import android.os.Parcel;
import android.os.Parcelable;

import com.riverside.skeleton.android.widget.commonselect.business.BaseBiz;

/**
 * 共通选择画面设置类    1.0
 * b_e  2018/02/16
 */
public class CommonSelectOptions implements Parcelable {
    public static final String COMMONSELECT_OPTION = "COMMONSELECT_OPTION";
    public static final String RESULT_DATA_TITLE = "title";
    public static final String RESULT_DATA_VALUE = "value";
    public static final String RESULT_DATA_SUBVALUE = "subValue";
    public static final String RESULT_DATA_CRITERIA = "list_criteria";

    public enum SEARCH_FLAG {NOT_HAVE, HAVE}

    private String titleName;
    private int choiceMode;
    private SEARCH_FLAG hasSearch = SEARCH_FLAG.NOT_HAVE; //0:不显示查询框 1:显示查询框
    private String[] checkedValue;
    private Class<? extends BaseBiz> listGenerator;
    private String[] listCriteria;
    private String listEmptyText = "";

    public CommonSelectOptions() {
        super();
    }

    private CommonSelectOptions(Parcel in) throws ClassNotFoundException {
        readFromParcel(in);
    }

    /**
     * 读取缓存数据
     *
     * @param in
     * @throws ClassNotFoundException
     */
    private void readFromParcel(Parcel in) throws ClassNotFoundException {
        titleName = in.readString();
        choiceMode = in.readInt();
        hasSearch = SEARCH_FLAG.valueOf(in.readString());
        checkedValue = in.createStringArray();
        listGenerator = (Class<? extends BaseBiz>) Class.forName(in.readString());
        listCriteria = in.createStringArray();
        listEmptyText = in.readString();
    }

    /**
     * 缓存数据
     *
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titleName);
        dest.writeInt(choiceMode);
        dest.writeString(hasSearch.name());
        dest.writeStringArray(checkedValue);
        dest.writeString(listGenerator.getName());
        dest.writeStringArray(listCriteria);
        dest.writeString(listEmptyText);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 缓存创建者对象
     */
    public static final Creator<CommonSelectOptions> CREATOR = new Creator<CommonSelectOptions>() {
        @Override
        public CommonSelectOptions createFromParcel(Parcel in) {
            try {
                return new CommonSelectOptions(in);
            } catch (ClassNotFoundException e) {
                return null;
            }
        }

        @Override
        public CommonSelectOptions[] newArray(int size) {
            return new CommonSelectOptions[size];
        }
    };

    /**
     * 标题
     *
     * @return
     */
    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    /**
     * 选择模式
     *
     * @return
     */
    public int getChoiceMode() {
        return choiceMode;
    }

    public void setChoiceMode(int choiceMode) {
        this.choiceMode = choiceMode;
    }

    /**
     * 查询模式
     *
     * @return
     */
    public SEARCH_FLAG getHasSearch() {
        return hasSearch;
    }

    public void setHasSearch(SEARCH_FLAG hasSearch) {
        this.hasSearch = hasSearch;
    }

    /**
     * 默认选择值
     *
     * @return
     */
    public String[] getCheckedValue() {
        return checkedValue;
    }

    public void setCheckedValue(String... checkedValue) {
        this.checkedValue = checkedValue;
    }

    public Class<? extends BaseBiz> getListGenerator() {
        return listGenerator;
    }

    /**
     * 查询条件
     *
     * @return
     */
    public String[] getListCriteria() {
        return listCriteria;
    }

    public void setListGenerator(Class<? extends BaseBiz> listGenerator) {
        this.listGenerator = listGenerator;
    }

    public void setListCriteria(String... listCriteria) {
        this.listCriteria = listCriteria;
    }

    /**
     * 空状态显示内容
     *
     * @return
     */
    public String getListEmptyText() {
        return listEmptyText;
    }

    public void setListEmptyText(String listEmptyText) {
        this.listEmptyText = listEmptyText;
    }
}
