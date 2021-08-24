package zone.wim.coding;

import com.google.auto.service.AutoService;
import lombok.extern.java.Log;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.LinkedHashSet;
import java.util.Set;

@Log
@AutoService(Processor.class)
public class SelfCodingProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;
//    private Map<String, FactoryGroupedClasses> factoryClasses = new LinkedHashMap<String, FactoryGroupedClasses>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<String>();
        annotations.add(SelfCodingUnit.class.getCanonicalName());
        return annotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        log.info("test processing annotations");
        for (Element selfCodingElement : roundEnv.getElementsAnnotatedWith(SelfCodingUnit.class)) {

            // Check if a class has been annotated with @Factory
            if (selfCodingElement.getKind() != ElementKind.CLASS) {
                error(selfCodingElement, "Only classes can be annotated with @%s",
                        SelfCodingUnit.class.getSimpleName());
                return true; // Exit processing
            }
        }
        return false;   //TODO: oh?
    }

    private void error(Element e, String msg, Object... args) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                e);
    }

}
