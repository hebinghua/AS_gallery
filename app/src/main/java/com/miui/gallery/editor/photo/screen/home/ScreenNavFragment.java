package com.miui.gallery.editor.photo.screen.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.miui.gallery.R;
import com.miui.gallery.app.fragment.AndroidFragment;
import com.miui.gallery.editor.photo.core.common.model.DoodleData;
import com.miui.gallery.editor.photo.core.imports.text.TextConfig;
import com.miui.gallery.editor.photo.penengine.OperationManager;
import com.miui.gallery.editor.photo.penengine.ToolBoxView;
import com.miui.gallery.editor.photo.penengine.entity.Eraser;
import com.miui.gallery.editor.photo.penengine.entity.Mark;
import com.miui.gallery.editor.photo.penengine.entity.Mosaic;
import com.miui.gallery.editor.photo.penengine.entity.Pen;
import com.miui.gallery.editor.photo.penengine.entity.Shape;
import com.miui.gallery.editor.photo.penengine.entity.Text;
import com.miui.gallery.editor.photo.penengine.entity.Tool;
import com.miui.gallery.editor.photo.screen.entity.ScreenNavigatorData;
import com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicData;
import com.miui.gallery.util.logger.DefaultLogger;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public class ScreenNavFragment extends AndroidFragment {
    public Callback mCallback;
    public Context mContext;
    public ScreenNavigatorData mMosaicData;
    public OperationManager mOperationManager;
    public ScreenNavigatorData mTextData;
    public ToolBoxView mTooBoxView;
    public ToolBoxView.ToolChangeListener mToolChangeListener = new ToolBoxView.ToolChangeListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenNavFragment.1
        {
            ScreenNavFragment.this = this;
        }

        @Override // com.miui.gallery.editor.photo.penengine.ToolBoxView.ToolChangeListener
        public void onToolSelect(Tool tool) {
            DefaultLogger.d("NavFragment_", "onToolSelect: " + tool.toString());
            switch (AnonymousClass2.$SwitchMap$com$miui$gallery$editor$photo$penengine$entity$Tool$ToolType[tool.getToolType().ordinal()]) {
                case 1:
                    ScreenNavFragment.this.mCallback.onPenSelect((Pen) tool);
                    return;
                case 2:
                    ScreenNavFragment.this.mCallback.onMarkSelect((Mark) tool);
                    return;
                case 3:
                    ScreenNavFragment.this.mCallback.onMosaicSelect((Mosaic) tool, ScreenNavFragment.this.mMosaicData);
                    return;
                case 4:
                    ScreenNavFragment.this.mCallback.onEraserSelect((Eraser) tool);
                    return;
                case 5:
                    ScreenNavFragment.this.mCallback.onTextSelect((Text) tool);
                    return;
                case 6:
                    Shape shape = (Shape) tool;
                    ScreenNavFragment.this.mOperationManager.getDoodleOperation().setDoodleData(shape.getDoodleData(), shape.getCurrentSelectIndex());
                    ScreenNavFragment.this.mOperationManager.getDoodleOperation().setColor(shape.getColor());
                    ScreenNavFragment.this.mCallback.onShapeSelect(shape);
                    return;
                default:
                    return;
            }
        }

        @Override // com.miui.gallery.editor.photo.penengine.ToolBoxView.ToolChangeListener
        public void onBrushColorChange(int i) {
            ScreenNavFragment.this.mOperationManager.getDoodleOperation().setColor(i);
        }

        @Override // com.miui.gallery.editor.photo.penengine.ToolBoxView.ToolChangeListener
        public void onBrushSizeChange(int i) {
            DefaultLogger.d("NavFragment_", "onBrushSizeChange: " + i);
            ScreenNavFragment.this.mOperationManager.getDoodleOperation().setSize(i);
        }

        @Override // com.miui.gallery.editor.photo.penengine.ToolBoxView.ToolChangeListener
        public void onBrushAlphaChange(float f) {
            DefaultLogger.d("NavFragment_", "onBrushAlphaChange: " + f);
            ScreenNavFragment.this.mOperationManager.getDoodleOperation().setAlpha(f);
        }

        @Override // com.miui.gallery.editor.photo.penengine.ToolBoxView.ToolChangeListener
        public void onMosaicDataChange(MosaicData mosaicData, int i) {
            DefaultLogger.d("NavFragment_", "onMosaicDataChange: " + mosaicData.name);
            ScreenNavFragment.this.mOperationManager.getScreenMosaicOperation().setMosaicData(mosaicData, i);
        }

        @Override // com.miui.gallery.editor.photo.penengine.ToolBoxView.ToolChangeListener
        public void onMosaicPaintSizeChange(int i) {
            DefaultLogger.d("NavFragment_", "onMosaicPaintSizeChange: " + i);
            ScreenNavFragment.this.mOperationManager.getScreenMosaicOperation().setMosaicPaintSize(i);
        }

        @Override // com.miui.gallery.editor.photo.penengine.ToolBoxView.ToolChangeListener
        public void onTextChange(TextConfig textConfig, int i) {
            ScreenNavFragment.this.mOperationManager.getScreenTextOperation().add(textConfig, i);
        }

        @Override // com.miui.gallery.editor.photo.penengine.ToolBoxView.ToolChangeListener
        public void onShapeChange(DoodleData doodleData, int i) {
            ScreenNavFragment.this.mOperationManager.getDoodleOperation().setDoodleData(doodleData, i);
        }

        @Override // com.miui.gallery.editor.photo.penengine.ToolBoxView.ToolChangeListener
        public void onShapeColorChange(int i) {
            ScreenNavFragment.this.mOperationManager.getDoodleOperation().setColor(i);
        }
    };

    /* loaded from: classes2.dex */
    public interface Callback {
        boolean onEraserSelect(Eraser eraser);

        boolean onMarkSelect(Mark mark);

        boolean onMosaicSelect(Mosaic mosaic, ScreenNavigatorData screenNavigatorData);

        boolean onPenSelect(Pen pen);

        void onRedoClick();

        void onSaveClick();

        boolean onShapeSelect(Shape shape);

        boolean onTextSelect(Text text);

        void onUndoClick();
    }

    /* renamed from: $r8$lambda$HT-ay8Mm2FP81BAShL5oeeYKsjI */
    public static /* synthetic */ void m930$r8$lambda$HTay8Mm2FP81BAShL5oeeYKsjI(ScreenNavFragment screenNavFragment, View view) {
        screenNavFragment.lambda$onViewCreated$1(view);
    }

    public static /* synthetic */ void $r8$lambda$n0yzYKMqCuk1zpXgOEdYxZajXyY(ScreenNavFragment screenNavFragment, View view) {
        screenNavFragment.lambda$onViewCreated$0(view);
    }

    /* renamed from: $r8$lambda$uOVlFaYC01-AAuMGYSqkdCm_p1U */
    public static /* synthetic */ void m931$r8$lambda$uOVlFaYC01AAuMGYSqkdCm_p1U(ScreenNavFragment screenNavFragment, View view) {
        screenNavFragment.lambda$onViewCreated$2(view);
    }

    @Override // com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mMosaicData = new ScreenNavigatorData(4);
        this.mTextData = new ScreenNavigatorData(3);
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        this.mOperationManager = new OperationManager((AppCompatActivity) context);
    }

    public static ScreenNavFragment newInstance() {
        return new ScreenNavFragment();
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.screen_editor_tool_layout, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        ToolBoxView toolBoxView = (ToolBoxView) view.findViewById(R.id.tool_box);
        this.mTooBoxView = toolBoxView;
        toolBoxView.setOnToolChangeListener(this.mToolChangeListener);
        this.mTooBoxView.setSaveClickListener(new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenNavFragment$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ScreenNavFragment.$r8$lambda$n0yzYKMqCuk1zpXgOEdYxZajXyY(ScreenNavFragment.this, view2);
            }
        });
        this.mTooBoxView.setUndoClickListener(new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenNavFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ScreenNavFragment.m930$r8$lambda$HTay8Mm2FP81BAShL5oeeYKsjI(ScreenNavFragment.this, view2);
            }
        });
        this.mTooBoxView.setRedoClickListener(new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenNavFragment$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ScreenNavFragment.m931$r8$lambda$uOVlFaYC01AAuMGYSqkdCm_p1U(ScreenNavFragment.this, view2);
            }
        });
    }

    public /* synthetic */ void lambda$onViewCreated$0(View view) {
        this.mCallback.onSaveClick();
    }

    public /* synthetic */ void lambda$onViewCreated$1(View view) {
        this.mCallback.onUndoClick();
    }

    public /* synthetic */ void lambda$onViewCreated$2(View view) {
        this.mCallback.onRedoClick();
    }

    /* renamed from: com.miui.gallery.editor.photo.screen.home.ScreenNavFragment$2 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass2 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$editor$photo$penengine$entity$Tool$ToolType;

        static {
            int[] iArr = new int[Tool.ToolType.values().length];
            $SwitchMap$com$miui$gallery$editor$photo$penengine$entity$Tool$ToolType = iArr;
            try {
                iArr[Tool.ToolType.PEN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$penengine$entity$Tool$ToolType[Tool.ToolType.MARK.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$penengine$entity$Tool$ToolType[Tool.ToolType.MOSAIC.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$penengine$entity$Tool$ToolType[Tool.ToolType.ERASER.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$penengine$entity$Tool$ToolType[Tool.ToolType.TEXT.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$penengine$entity$Tool$ToolType[Tool.ToolType.SHAPE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    public void setUndoEnable(boolean z) {
        ToolBoxView toolBoxView = this.mTooBoxView;
        if (toolBoxView != null) {
            toolBoxView.setUndoEnable(z);
        }
    }

    public void setRedoEnable(boolean z) {
        ToolBoxView toolBoxView = this.mTooBoxView;
        if (toolBoxView != null) {
            toolBoxView.setRedoEnable(z);
        }
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }
}
