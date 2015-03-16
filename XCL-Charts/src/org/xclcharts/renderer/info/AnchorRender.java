/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 	
 * @Project XCL-Charts 
 * @Description Android图表基类库
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 2.1
 */
package org.xclcharts.renderer.info;

import org.xclcharts.common.DrawHelper;
import org.xclcharts.renderer.XEnum;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;

/**
 * @ClassName AnchorRender
 * @Description  绘制批注的形状
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 *  
 */

public class AnchorRender {
	
	private static AnchorRender instance = null;
	
	private RectF mRect= null;
	
	private Paint mPaintText = null;
	private Paint mPaintBg = null;
	
	public AnchorRender()
	{

	}
	
	public static synchronized AnchorRender getInstance()
	{
		if(instance == null)
		{
			instance = new AnchorRender();
		}
		return instance;
	}
	
	
	public void renderAnchor(Canvas canvas,
					AnchorDataPoint pAnchor, float cx, float cy,float cradius,
					float left,float top,float right, float bottom) {
		
		if(null == pAnchor) return;
		
		
		float radius = pAnchor.getRadius();
		
		
		
		
		switch(pAnchor.getAreaStyle())
		{
		case FILL:
			getBgPaint().setStyle(Style.FILL);
			break;
		case STROKE:
			getBgPaint().setStyle(Style.STROKE);
			break;		
		}		
		getBgPaint().setColor(pAnchor.getBgColor());
		
		float width = getBgPaint().getStrokeWidth();
		if( pAnchor.getLineWidth() > -1 )
		{			
			getBgPaint().setStrokeWidth(pAnchor.getLineWidth());
		}
		
		//XEnum.LineStyle lstyle = pAnchor.getLineStyle();
		
		switch (pAnchor.getAnchorStyle()) {
		case CAPRECT:
			renderCapBox(canvas,pAnchor,cx, cy, radius);
			break;
		//case DOT:										
		//	canvas.drawCircle(cx, cy, radius, getBgPaint());	
		//	break;
		case RECT:	
			renderRect(canvas,getBgPaint(),radius,cx, cy);			
			break;
		case CIRCLE:			
			canvas.drawCircle(cx, cy, radius, getBgPaint());
			break;			
		case VLINE:	
			// DrawHelper.getInstance().drawLine(lstyle, 
			//		 cx, top,cx, bottom,canvas, getBgPaint());	
			canvas.drawLine(cx, top,cx, bottom, getBgPaint());
			break;
		case HLINE:	
			 //DrawHelper.getInstance().drawLine(lstyle, 
			//		 left, cy,right, cy,canvas, getBgPaint());
			canvas.drawLine(left, cy,right, cy, getBgPaint());
			break;
		case TOBOTTOM:				
			// DrawHelper.getInstance().drawLine(XEnum.LineStyle.DOT, //lstyle, 
			//		 cx, cy + cradius,cx,bottom,canvas, getBgPaint());	
			canvas.drawLine(cx, cy + cradius,cx,bottom, getBgPaint());
			break;
		case TOTOP:		
			//DrawHelper.getInstance().drawLine(lstyle, 
			//		cx, cy - cradius,cx, top,canvas, getBgPaint());	
			canvas.drawLine(cx, cy - cradius,cx, top, getBgPaint());
			break;
		case TOLEFT:	
			//DrawHelper.getInstance().drawLine(lstyle, 
			//		cx - cradius, cy,left, cy,canvas, getBgPaint());
			canvas.drawLine(cx - cradius, cy,left, cy, getBgPaint());
			break;
		case TORIGHT:	
			//DrawHelper.getInstance().drawLine(lstyle, 
			//		cx + cradius, cy,right, cy,canvas, getBgPaint());	
			canvas.drawLine(cx + cradius, cy,right, cy, getBgPaint());
			break;
		default:
		}			
								
		if(pAnchor.getAnchor() != "" && 
				pAnchor.getAnchorStyle() != XEnum.AnchorStyle.CAPRECT)
		{
			getTextPaint().setColor(pAnchor.getTextColor());
			getTextPaint().setTextSize(pAnchor.getTextSize());
			canvas.drawText(pAnchor.getAnchor(), cx, cy, getTextPaint());
		}
		
		getBgPaint().setStrokeWidth(width);
	}
	
	
	private void renderCapBox(Canvas canvas,AnchorDataPoint pAnchor,
					float cirX,float cirY,float radius){
		
		
		float angleW = pAnchor.getCapRectW() / 2;//20.f;
		float angleH = pAnchor.getCapRectH(); //10.f;
		
		if(Float.compare(radius, angleW) == -1 || Float.compare(radius, angleW) == 0)
		{
			radius = angleW + 10.f;
		}		
		
		float fontH = angleH + 5.f;		
		float extW = angleW + radius;
	
		if(pAnchor.getAnchor() != "")
		{
			fontH = DrawHelper.getInstance().calcTextHeight(getTextPaint(), pAnchor.getAnchor());
			fontH += 5.f;		
			
			float FontW = DrawHelper.getInstance().getTextWidth(getTextPaint(), pAnchor.getAnchor());			
			if( Float.compare( extW * 2,FontW) == -1 ) extW = FontW/2;			
			extW += 3.f;
		}			
				
		Path path = new Path();
		path.moveTo(cirX, cirY);
		path.lineTo(cirX - angleW , cirY - angleH);
		path.lineTo(cirX - extW , cirY - angleH);		
		path.lineTo(cirX - extW,  cirY - angleH - fontH);
		path.lineTo(cirX + extW,  cirY - angleH - fontH);
		path.lineTo(cirX + extW,  cirY - angleH );
		path.lineTo(cirX + angleW,  cirY - angleH );
		path.lineTo(cirX, cirY);
		path.close();
		canvas.drawPath(path, getBgPaint());	
		
		if(pAnchor.getAnchor() != "")
		{
			getTextPaint().setColor(pAnchor.getTextColor());
			getTextPaint().setTextSize(pAnchor.getTextSize());
			canvas.drawText(pAnchor.getAnchor(), cirX,  cirY - angleH - fontH/3, getTextPaint());			
		}
		path.reset();
	}

	
	
	private void renderRect(Canvas canvas,Paint paint,
							float radius,float cirX,float cirY )
	{
		
		if(null == mRect)mRect = new RectF();
		
		mRect.left =  (cirX - radius);
		mRect.top =   (cirY - radius); 
		mRect.right =  (cirX + radius);
		mRect.bottom = (cirY + radius);
		canvas.drawRect(mRect,getBgPaint());		
		mRect.setEmpty();
	}
	
	private Paint getTextPaint()
	{
		if(null == mPaintText)
		{
			mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);		
			mPaintText.setTextAlign(Align.CENTER);
		}
		return mPaintText;
	}
	
	private Paint getBgPaint()
	{
		if(null == mPaintBg) mPaintBg = new Paint(Paint.ANTI_ALIAS_FLAG);	
		mPaintBg.setStrokeWidth(2);
		return mPaintBg;
	}
	
	
	
}
