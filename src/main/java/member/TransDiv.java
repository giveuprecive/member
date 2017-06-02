package member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransDiv {

	public static Map<String,String> div(String[] wavlist,List<String> urls){
		Map<String,String> result = new HashMap<String,String>();
		int num = wavlist.length % urls.size()==0?wavlist.length / urls.size():wavlist.length / urls.size() + 1;
		for(int i=0;i<urls.size();i++){
			StringBuffer voiceLis = new StringBuffer();
			for(int j=i*num;j<(i+1)*num;j++){
				if(j<wavlist.length){
					voiceLis.append(wavlist[j]).append(",");
				}
			}
			result.put(voiceLis.toString(), urls.get(i));
			System.out.println(result);
			//sendVoiceLists(voiceLis.toString(), urls.get(i), taskUrlString, voiceConfigId);
		}
		
		return result;
	
	}
}
